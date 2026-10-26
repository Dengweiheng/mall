<template>
  <div class="stock-manage-container">
    <el-tabs v-model="activeTab">
      <!-- 出入库记录 -->
      <el-tab-pane label="库存流水(明细)" name="records">
        <el-card shadow="never">
          <el-table :data="recordData" v-loading="loading" border stripe>
            <el-table-column prop="createTime" label="时间" width="180" />
            <el-table-column prop="productName" label="商品名称" />
            <el-table-column prop="type" label="类型" width="100">
              <template #default="scope">
                <el-tag :type="getTypeTag(scope.row.type)">
                  {{ getTypeName(scope.row.type) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="changeAmount" label="变动数量" width="100">
              <template #default="scope">
                <span :style="{ color: (scope.row.changeAmount || 0) > 0 ? '#67C23A' : '#F56C6C' }">
                  {{ (scope.row.changeAmount || 0) > 0 ? '+' : '' }}{{ scope.row.changeAmount || 0 }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注说明" show-overflow-tooltip />
          </el-table>
          <div class="pagination-box mt-20">
            <el-pagination
              v-model:current-page="pageNum"
              v-model:page-size="pageSize"
              :total="total"
              layout="total, prev, pager, next"
              @current-change="loadRecords"
            />
          </div>
        </el-card>
      </el-tab-pane>

      <!-- 手动操作 -->
      <el-tab-pane label="手动出入库" name="action">
        <el-card shadow="never" style="max-width: 600px">
          <el-form :model="stockForm" label-width="100px">
            <el-form-item label="选择商品">
              <el-select v-model="stockForm.productId" filterable placeholder="搜索商品" style="width: 100%">
                <el-option 
                  v-for="item in productOptions" 
                  :key="item.id" 
                  :label="item.name + ' (SKU: ' + item.sku + ')'" 
                  :value="item.id" 
                />
              </el-select>
            </el-form-item>
            <el-form-item label="操作类型">
              <el-radio-group v-model="stockForm.type">
                <el-radio :label="1">手动入库</el-radio>
                <el-radio :label="2">手动出库</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="变动数量">
              <el-input-number v-model="stockForm.amount" :min="1" />
              <div v-if="stockForm.type === 2" class="tip">出库数量将从现有库存扣除</div>
            </el-form-item>
            <el-form-item label="备注说明">
              <el-input v-model="stockForm.remark" type="textarea" placeholder="如：厂家到货、仓库损坏等" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="submitting" @click="handleStockAction">提交处理</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const activeTab = ref('records')
const loading = ref(false)
const recordData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const user = JSON.parse(localStorage.getItem('erp-user') || '{}')

const loadRecords = async () => {
  loading.value = true
  try {
    const res = await request.get('/product/stock-records', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value }
    })
    recordData.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const getTypeName = (t) => {
  const map = { 1: '入库', 2: '出库', 3: '调整', 4: '初始数据' }
  return map[t] || '其他'
}

const getTypeTag = (t) => {
  const map = { 1: 'success', 2: 'danger', 3: 'warning', 4: 'info' }
  return map[t] || ''
}

// 手动操作
const submitting = ref(false)
const productOptions = ref([])
const stockForm = reactive({
  productId: null,
  type: 1, // 1-in, 2-out
  amount: 0,
  remark: ''
})

const loadProducts = async () => {
  // 不分页获取所有商品供选择
  const res = await request.get('/product/page', { params: { pageNum: 1, pageSize: 1000 } })
  productOptions.value = res.list
}

const handleStockAction = async () => {
  if (!stockForm.productId) return ElMessage.warning('请先选择商品')
  if (stockForm.amount <= 0) return ElMessage.warning('数量必须大于0')
  
  submitting.value = true
  try {
    const endpoint = stockForm.type === 1 ? 'stock-in' : 'stock-out'
    
    // 使用 URLSearchParams 构建请求参数 (后端 @RequestParam 接收)
    const params = new URLSearchParams()
    params.append('amount', stockForm.amount)
    params.append('operatorId', user.id)
    params.append('remark', stockForm.remark || '手动操作')

    // 后端这几个接口是 POST 方法
    await request.post(`/product/${stockForm.productId}/${endpoint}?${params.toString()}`)
    ElMessage.success('操作成功')
    
    // 重置并切换
    stockForm.productId = null
    stockForm.amount = 0
    stockForm.remark = ''
    activeTab.value = 'records'
    loadRecords()
  } catch (err) {
    console.error(err)
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadRecords()
  loadProducts()
})
</script>

<style scoped>
.stock-manage-container { padding: 10px; }
.mt-20 { margin-top: 20px; }
.pagination-box { display: flex; justify-content: flex-end; }
.tip { font-size: 12px; color: #999; margin-top: 5px; }
</style>
