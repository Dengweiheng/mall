<template>
  <div class="cart-container">
    <el-page-header @back="router.go(-1)" title="购物车" content="确认明细" />
    
    <div class="cart-main mt-20">
      <el-table :data="cartList" stripe style="width: 100%" v-if="cartList.length > 0">
        <el-table-column label="商品信息" min-width="250">
          <template #default="scope">
            <div class="product-cell">
              <el-image :src="scope.row.image" style="width: 60px; height: 60px; border-radius: 4px" />
              <div class="product-meta">
                <div class="name">{{ scope.row.name }}</div>
                <div class="sku">SKU: {{ scope.row.sku }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="单价" width="120">
          <template #default="scope">￥{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column label="数量" width="180">
          <template #default="scope">
            <el-input-number v-model="scope.row.count" :min="1" :max="scope.row.stock" size="small" @change="saveCart" />
          </template>
        </el-table-column>
        <el-table-column label="小计" width="120">
          <template #default="scope">
            <span class="subtotal">￥{{ (scope.row.price * scope.row.count).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="danger" link @click="removeItem(scope.$index)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-else description="购物车空空如也" />
    </div>

    <!-- 底部结算栏 -->
    <div class="cart-footer mt-20" v-if="cartList.length > 0">
      <div class="total-info">
        共 <span>{{ totalCount }}</span> 件商品，总计金额：
        <span class="total-price">￥{{ totalPrice.toFixed(2) }}</span>
      </div>
      <el-button type="primary" size="large" @click="submitBatchOrder" :loading="loading">立即结算下单</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const router = useRouter()
const cartList = ref([])
const loading = ref(false)
const user = JSON.parse(localStorage.getItem('erp-user') || '{}')

const loadCart = () => {
  cartList.value = JSON.parse(localStorage.getItem('erp-cart') || '[]')
}

const saveCart = () => {
  localStorage.setItem('erp-cart', JSON.stringify(cartList.value))
}

const removeItem = (index) => {
  cartList.value.splice(index, 1)
  saveCart()
}

const totalCount = computed(() => cartList.value.reduce((sum, item) => sum + item.count, 0))
const totalPrice = computed(() => cartList.value.reduce((sum, item) => sum + (item.price * item.count), 0))

const submitBatchOrder = async () => {
  if (cartList.value.length === 0) return
  
  loading.value = true
  try {
    const orderItems = cartList.value.map(i => ({
      productId: i.id,
      quantity: i.count
    }))
    
    await request.post('/order', {
      userId: user.id,
      items: orderItems,
      remark: '购物车批量下单'
    })
    
    ElMessage.success('批量下单成功！')
    localStorage.removeItem('erp-cart') // 清空购物车
    cartList.value = []
    setTimeout(() => router.push('/order/my'), 1000)
  } finally {
    loading.value = false
  }
}

onMounted(loadCart)
</script>

<style scoped>
.cart-container { padding: 20px; max-width: 1000px; margin: 0 auto; }
.mt-20 { margin-top: 20px; }

.product-cell { display: flex; gap: 15px; align-items: center; }
.product-meta { text-align: left; }
.product-meta .name { font-weight: bold; font-size: 14px; }
.product-meta .sku { font-size: 12px; color: #999; margin-top: 4px; }

.subtotal { color: #ff4d4f; font-weight: bold; }

.cart-footer {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 -2px 10px rgba(0,0,0,0.05);
}

.total-info { font-size: 15px; }
.total-info span { color: #ff4d4f; font-weight: bold; }
.total-price { font-size: 24px !important; margin-left: 10px; }
</style>
