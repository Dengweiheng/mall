<template>
  <div class="order-container">
    <el-card>
      <template #header>
        <div class="header">
          <span>我的订单列表</span>
          <el-radio-group v-model="status" size="small" @change="loadData">
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button :label="0">待支付</el-radio-button>
            <el-radio-button :label="1">待发货</el-radio-button>
            <el-radio-button :label="2">已发货</el-radio-button>
            <el-radio-button :label="3">已完成</el-radio-button>
          </el-radio-group>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="orderNo" label="订单编号" min-width="180" />
        <el-table-column prop="totalAmount" label="总金额" width="100">
          <template #default="scope">
            <span class="price">￥{{ scope.row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" effect="dark" size="small">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="160">
           <template #default="scope">
             {{ formatTime(scope.row.createTime) }}
           </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button v-if="scope.row.status === 0" type="success" size="small" @click="handlePay(scope.row)">支付</el-button>
            <el-button v-if="scope.row.status === 2" type="warning" size="small" @click="confirmReceipt(scope.row)">确认收货</el-button>
            <el-button type="primary" link size="small" @click="viewDetail(scope.row)">详情</el-button>
            <el-button v-if="scope.row.status === 0" type="danger" link size="small" @click="cancelOrder(scope.row)">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 模拟支付弹窗 -->
    <el-dialog v-model="payVisible" title="模拟在线支付" width="300px" center>
      <div style="text-align: center">
        <el-icon size="60" color="#67C23A"><Checked /></el-icon>
        <div style="margin-top: 20px; font-size: 16px;">确认支付该笔订单？</div>
        <div class="price" style="font-size: 24px; margin-top: 10px;">￥{{ currentOrder?.totalAmount }}</div>
      </div>
      <template #footer>
        <el-button type="primary" @click="confirmPay" :loading="payLoading" style="width: 100%">确认支付</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus'
import request from '../../utils/request'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const status = ref('')
const user = JSON.parse(localStorage.getItem('erp-user') || '{}')

const payVisible = ref(false)
const payLoading = ref(false)
const currentOrder = ref(null)

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/order/page', {
      params: {
        userId: user.id,
        status: status.value === '' ? null : status.value,
        pageNum: 1,
        pageSize: 100
      }
    })
    tableData.value = res.list
  } finally {
    loading.value = false
  }
}

const handlePay = (row) => {
  currentOrder.value = row
  payVisible.value = true
}

const confirmPay = async () => {
  payLoading.value = true
  try {
    // 模拟后端逻辑：后端pay接口需要adminId(这里暂且传1)
    await request.put(`/order/${currentOrder.value.id}/pay?adminId=1`)
    ElMessage.success('支付成功，请等待发货')
    payVisible.value = false
    loadData()
  } finally {
    payLoading.value = false
  }
}

const confirmReceipt = (row) => {
  ElMessageBox.confirm('确认已收到货物？确认后订单将变成“已完成”状态', '确认收货', {
    type: 'success'
  }).then(async () => {
    await request.put(`/order/${row.id}/complete`)
    ElMessage.success('收货成功，订单已完成')
    loadData()
  })
}

const getStatusName = (s) => {
  const map = { 0: '待支付', 1: '待发货', 2: '已发货', 3: '已完成' }
  return map[s] || '未知'
}

const getStatusType = (s) => {
  const map = { 0: 'warning', 1: 'primary', 2: 'info', 3: 'success' }
  return map[s] || ''
}

const formatTime = (time) => {
  if (!time) return '-'
  return time.replace('T', ' ').substring(0, 16)
}

const cancelOrder = (row) => {
  ElMessageBox.confirm('确定要取消该订单吗？库存将自动退回', '提示', {
    type: 'warning'
  }).then(async () => {
    await request.put(`/order/${row.id}/cancel`)
    ElMessage.success('订单已取消')
    loadData()
  })
}

const viewDetail = (row) => {
  router.push({ name: 'OrderDetail', params: { id: row.id } })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.price { color: #f56c6c; font-weight: bold; }
</style>
