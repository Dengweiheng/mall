<template>
  <div class="order-detail-container">
    <el-page-header @back="router.go(-1)" title="订单详情" />
    
    <div v-if="order" class="detail-content mt-20">
      <!-- 状态卡片 -->
      <el-card shadow="never" class="status-card">
        <div class="status-box">
          <div class="status-text">
            订单状态：
            <el-tag :type="getStatusType(order.status)" effect="dark">
              {{ getStatusName(order.status) }}
            </el-tag>
          </div>
          <div class="status-actions">
            <!-- 待支付可直接支付 -->
            <el-button v-if="order.status === 0" type="success" @click="handlePay">立即支付</el-button>
            <!-- 已发货可确认收货 -->
            <el-button v-if="order.status === 2" type="warning" @click="confirmReceipt">确认收货</el-button>
          </div>
          <div class="order-no">订单编号：{{ order.orderNo }}</div>
        </div>
      </el-card>

      <!-- 基本信息与商品清单 -->
      <el-row :gutter="20" class="mt-20">
        <el-col :span="16">
          <el-card header="商品列表" shadow="never">
            <el-table :data="order.items" stripe border>
              <el-table-column label="商品信息" min-width="200">
                <template #default="scope">
                  <div class="product-cell">
                    <div class="product-name">{{ scope.row.productName }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="productPrice" label="单价" width="120">
                <template #default="scope">￥{{ scope.row.productPrice }}</template>
              </el-table-column>
              <el-table-column prop="quantity" label="数量" width="100" />
              <el-table-column prop="totalPrice" label="小计" width="120">
                <template #default="scope">
                  <span class="price">￥{{ scope.row.totalPrice }}</span>
                </template>
              </el-table-column>
            </el-table>
            <div class="order-total mt-20">
              应付总额：<span>￥{{ order.totalAmount }}</span>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card header="订单信息" shadow="never">
            <el-descriptions :column="1" border>
              <el-descriptions-item label="下单账号">{{ order.username }}</el-descriptions-item>
              <el-descriptions-item label="下单时间">{{ formatTime(order.createTime) }}</el-descriptions-item>
              <el-descriptions-item label="订单备注">{{ order.remark || '无' }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <el-skeleton v-else :rows="10" animated />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../../utils/request'

import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const order = ref(null)

const loadDetail = async () => {
  const id = route.params.id
  try {
    const res = await request.get(`/order/${id}`)
    order.value = res
  } catch (err) {
    console.error(err)
  }
}

const handlePay = () => {
  ElMessageBox.confirm(`确认支付金额 ￥${order.value.totalAmount}？`, '确认支付', {
    type: 'success'
  }).then(async () => {
    await request.put(`/order/${order.value.id}/pay?adminId=1`)
    ElMessage.success('支付成功')
    loadDetail()
  })
}

const confirmReceipt = () => {
  ElMessageBox.confirm('确认已收到货物？确认后订单完成', '确认收货', {
    type: 'success'
  }).then(async () => {
    await request.put(`/order/${order.value.id}/complete`)
    ElMessage.success('订单已收货完结')
    loadDetail()
  })
}

const getStatusName = (s) => {
  const map = { 0: '待支付', 1: '待发货', 2: '已发货', 3: '已完成', 4: '已取消' }
  return map[s] || '未知'
}

const getStatusType = (s) => {
  const map = { 0: 'warning', 1: 'primary', 2: 'info', 3: 'success', 4: 'danger' }
  return map[s] || ''
}

const formatTime = (time) => {
  if (!time) return '-'
  return time.replace('T', ' ').substring(0, 19)
}

onMounted(loadDetail)
</script>

<style scoped>
.order-detail-container { padding: 20px; max-width: 1200px; margin: 0 auto; }
.mt-20 { margin-top: 20px; }

.status-card { background: #fdfdfd; }
.status-box { display: flex; justify-content: space-between; align-items: center; }
.status-text { font-size: 18px; font-weight: bold; }
.order-no { color: #999; font-size: 14px; }

.order-total { text-align: right; font-size: 18px; font-weight: bold; }
.order-total span { color: #f56c6c; font-size: 24px; }

.price { color: #f56c6c; font-weight: bold; }

.product-cell { text-align: left; }
.product-name { font-weight: bold; }
</style>
