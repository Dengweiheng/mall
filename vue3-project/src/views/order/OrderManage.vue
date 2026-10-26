<template>
  <div class="order-manage-container">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.username" placeholder="请输入客户名" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 150px">
            <el-option label="待支付" :value="0" />
            <el-option label="待发货" :value="1" />
            <el-option label="已发货" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="mt-20">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="orderNo" label="订单编号" width="220" />
        <el-table-column label="客户信息" width="150">
          <template #default="scope">
             {{ scope.row.username || '普通用户' }}
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template #default="scope">
            <span class="price-tag">￥{{ scope.row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="时间" width="180" />
        <el-table-column label="操作" fixed="right" min-width="200">
          <template #default="scope">
            <!-- 待支付 -> 线下确认支付 -->
            <el-button 
              v-if="scope.row.status === 0" 
              type="success" 
              size="small" 
              @click="handlePay(scope.row)"
            >确认到账</el-button>
            
            <!-- 待发货 -> 点击发货 -->
            <el-button 
              v-if="scope.row.status === 1" 
              type="primary" 
              size="small" 
              @click="handleShip(scope.row)"
            >点击发货</el-button>



            <el-button type="info" size="small" link @click="viewDetail(scope.row)">详情</el-button>
            
            <el-button 
              v-if="scope.row.status <= 1" 
              type="danger" 
              size="small" 
              link 
              @click="handleCancel(scope.row)"
            >取消单</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-box mt-20">
        <el-pagination
          v-model:current-page="searchForm.pageNum"
          v-model:page-size="searchForm.pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="订单明细" width="700px">
      <div v-if="orderDetail" class="detail-content">
        <el-descriptions border :column="2">
          <el-descriptions-item label="订单号">{{ orderDetail.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ getStatusName(orderDetail.status) }}</el-descriptions-item>
          <el-descriptions-item label="总金额" class-name="price-tag">￥{{ orderDetail.totalAmount }}</el-descriptions-item>
          <el-descriptions-item label="下单日期">{{ orderDetail.createTime }}</el-descriptions-item>
        </el-descriptions>

        <h4 class="mt-20">包含商品</h4>
        <el-table :data="orderDetail.items" border size="small" class="mt-10">
          <el-table-column prop="productName" label="商品型号" />
          <el-table-column prop="productPrice" label="单价" width="100" />
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column prop="totalPrice" label="小计" width="100">
             <template #default="scope">￥{{ scope.row.totalPrice }}</template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const user = JSON.parse(localStorage.getItem('erp-user') || '{}')

const searchForm = reactive({
  orderNo: '',
  username: '',
  status: null,
  pageNum: 1,
  pageSize: 10
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/order/page', { params: searchForm })
    tableData.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.orderNo = ''
  searchForm.username = ''
  searchForm.status = null
  loadData()
}

const getStatusName = (s) => {
  const map = { 0: '待支付', 1: '待发货', 2: '已发货', 3: '已完成', 4: '已取消' }
  return map[s] || '未知'
}

const getStatusType = (s) => {
  const map = { 0: 'warning', 1: 'primary', 2: 'info', 3: 'success', 4: 'danger' }
  return map[s] || ''
}

// 按钮操作
const handlePay = (row) => {
  ElMessageBox.confirm('确认已收到该笔订单款项？确认后将转入“待发货”并自动记录财务收入。', '支付核销', {
    type: 'success'
  }).then(async () => {
    await request.put(`/order/${row.id}/pay?adminId=${user.id}`)
    ElMessage.success('核销成功，请及时安排发货')
    loadData()
  })
}

const handleShip = (row) => {
  ElMessageBox.confirm('确认已经将商品打包交给物流公司？', '发货确认').then(async () => {
    await request.put(`/order/${row.id}/ship`)
    ElMessage.success('状态已改为“已发货”')
    loadData()
  })
}



const handleCancel = (row) => {
  ElMessageBox.confirm('确定要取消该订单吗？取消后库存将原路退回', '作废订单', {
    type: 'warning'
  }).then(async () => {
    await request.put(`/order/${row.id}/cancel`)
    ElMessage.success('订单已作废')
    loadData()
  })
}

// 详情逻辑
const detailVisible = ref(false)
const orderDetail = ref(null)
const viewDetail = async (row) => {
  const data = await request.get(`/order/${row.id}`)
  orderDetail.value = data
  detailVisible.value = true
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.order-manage-container { padding: 10px; }
.mt-20 { margin-top: 20px; }
.mt-10 { margin-top: 10px; }
.price-tag { color: #f56c6c; font-weight: bold; }
.pagination-box { display: flex; justify-content: flex-end; }
</style>
