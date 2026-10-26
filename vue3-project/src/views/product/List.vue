<template>
  <div class="mall-container">
    <!-- 顶部轮播图 -->
    <div class="mall-banner">
      <el-carousel height="300px" trigger="click" indicator-position="outside">
        <el-carousel-item v-for="(img, idx) in banners" :key="idx">
          <img :src="img" class="banner-img" />
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 顶栏：分类与搜索 -->
    <div class="mall-header">
      <div class="header-main">
        <el-input
          v-model="searchForm.name"
          placeholder="搜索您心仪的商品..."
          class="search-input"
          @keyup.enter="loadData"
        >
          <template #append>
            <el-button icon="Search" @click="loadData" />
          </template>
        </el-input>
        
        <template v-if="isLoggedIn">
          <el-badge :value="cartCount" :hidden="cartCount === 0" class="cart-badge">
            <el-button type="warning" icon="ShoppingCart" @click="goToCart">我的购物车</el-button>
          </el-badge>
        </template>
        <template v-else>
          <el-button type="primary" icon="User" @click="router.push('/login')">登录 / 注册</el-button>
        </template>
      </div>
      
      <div class="category-tags">
        <span 
          v-for="cat in categories" 
          :key="cat"
          :class="['tag-item', searchForm.category === cat ? 'active' : '']"
          @click="toggleCategory(cat)"
        >
          {{ cat }}
        </span>
      </div>
    </div>

    <!-- 商品列表 -->
    <div v-loading="loading" class="mall-content">
      <el-row :gutter="20" v-if="tableData.length > 0">
        <el-col 
          v-for="item in tableData" 
          :key="item.id" 
          :xs="24" :sm="12" :md="8" :lg="6" :xl="4"
        >
          <el-card class="product-card" :body-style="{ padding: '0px' }" shadow="hover">
            <div class="image-wrapper">
              <el-image 
                :src="item.image || 'https://via.placeholder.com/300'" 
                fit="cover" 
                class="product-image"
              />
              <div class="category-badge">{{ item.category }}</div>
            </div>
            
            <div class="product-info">
              <div class="product-name">{{ item.name }}</div>
              <div class="product-sku">库存: {{ item.stock }}</div>
              <div class="price-row">
                <span class="price-val">￥{{ item.price }}</span>
                <el-button type="primary" size="small" circle icon="Plus" @click="addToCart(item)" />
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-empty v-else description="暂无相关商品" />

      <!-- 分页 -->
      <div class="pagination-box">
        <el-pagination
          v-model:current-page="searchForm.pageNum"
          v-model:page-size="searchForm.pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="loadData"
        />
      </div>
    </div>

    <!-- 登录提醒弹窗 -->
    <el-dialog v-model="loginDialogVisible" title="请先登录" width="360px" center>
      <div style="text-align:center;padding:10px 0 20px">
        <el-icon style="font-size:48px;color:#409EFF;margin-bottom:12px"><Lock /></el-icon>
        <p style="color:#666;font-size:15px">请登录后再进行此操作</p>
      </div>
      <template #footer>
        <el-button @click="loginDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="router.push('/login')">立即登录</el-button>
      </template>
    </el-dialog>

    <!-- 极简购买弹窗 -->
    <el-dialog v-model="buyVisible" title="确认下单" width="360px" center>
      <div v-if="selectedProduct" class="buy-box">
        <div class="buy-target">
          <img :src="selectedProduct.image || 'https://via.placeholder.com/100'" />
          <div>
            <strong>{{ selectedProduct.name }}</strong>
            <p>单价：￥{{ selectedProduct.price }}</p>
          </div>
        </div>
        <div class="num-picker">
          <span>购买数量：</span>
          <el-input-number v-model="buyAmount" :min="1" :max="selectedProduct.stock" border />
        </div>
        <div class="total">
          总计：<span>￥{{ (selectedProduct.price * buyAmount).toFixed(2) }}</span>
        </div>
      </div>
      <template #footer>
        <el-button type="primary" style="width: 100%" @click="submitOrder" :loading="buyLoading">
          立即支付并下单
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'
import { useRouter } from 'vue-router'
import banner1 from '../../assets/1.png'
import banner2 from '../../assets/2.png'
import banner3 from '../../assets/3.png'
import banner4 from '../../assets/4.png'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const categories = ['全部', '手机', '办公', '食品']
const user = JSON.parse(localStorage.getItem('erp-user') || 'null')
const isLoggedIn = !!user

const banners = [banner1, banner2, banner3, banner4]
const loginDialogVisible = ref(false)

const searchForm = reactive({
  name: '',
  category: '',
  pageNum: 1,
  pageSize: 12
})

// 购物车逻辑
const cartCount = ref(0)
const updateCartCount = () => {
  const cart = JSON.parse(localStorage.getItem('erp-cart') || '[]')
  cartCount.value = cart.reduce((total, item) => total + item.count, 0)
}

const addToCart = (product) => {
  if (!isLoggedIn) {
    loginDialogVisible.value = true
    return
  }
  if (product.stock <= 0) return ElMessage.error('该商品库存不足')
  const cart = JSON.parse(localStorage.getItem('erp-cart') || '[]')
  const index = cart.findIndex(i => i.id === product.id)
  if (index > -1) {
    cart[index].count += 1
  } else {
    cart.push({ ...product, count: 1 })
  }
  localStorage.setItem('erp-cart', JSON.stringify(cart))
  updateCartCount()
  ElMessage.success('已加入购物车')
}

const goToCart = () => {
  if (!isLoggedIn) {
    loginDialogVisible.value = true
    return
  }
  router.push('/product/cart')
}

const loadData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm }
    if (params.category === '全部') params.category = ''
    const res = await request.get('/product/page', { params })
    tableData.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const toggleCategory = (cat) => {
  searchForm.category = cat
  searchForm.pageNum = 1
  loadData()
}

// 下单逻辑保留
const buyVisible = ref(false)
const buyLoading = ref(false)
const selectedProduct = ref(null)
const buyAmount = ref(1)

const openBuy = (product) => {
  selectedProduct.value = product
  buyAmount.value = 1
  buyVisible.value = true
}

const submitOrder = async () => {
  buyLoading.value = true
  try {
    await request.post('/order', {
      userId: user.id,
      items: [{ productId: selectedProduct.value.id, quantity: buyAmount.value }],
      remark: '商城下单'
    })
    ElMessage.success('订单已提交！')
    buyVisible.value = false
    loadData()
  } finally {
    buyLoading.value = false
  }
}

onMounted(() => {
  loadData()
  updateCartCount()
})
</script>

<style scoped>
.mall-container { padding: 0 20px 20px; max-width: 1400px; margin: 0 auto; }

.mall-banner { margin-bottom: 20px; border-radius: 12px; overflow: hidden; }
.banner-img { width: 100%; height: 100%; object-fit: cover; }

/* 顶栏样式 */
.mall-header {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 30px;
}
.header-main {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
}
.search-input { max-width: 500px; }
.cart-badge { margin-left: 10px; }

.category-tags { display: flex; justify-content: center; gap: 15px; }
.tag-item {
  padding: 5px 18px;
  cursor: pointer;
  border-radius: 20px;
  background: #fff;
  border: 1px solid #eee;
  color: #606266;
  transition: all 0.3s;
}
.tag-item:hover, .tag-item.active {
  background: #409eff;
  color: #fff;
  border-color: #409eff;
}

/* 卡片样式 */
.product-card {
  margin-bottom: 20px;
  border-radius: 12px;
  border: none;
  overflow: hidden;
  transition: all 0.3s;
}
.product-card:hover { transform: translateY(-8px); box-shadow: 0 10px 20px rgba(0,0,0,0.1); }

.image-wrapper { position: relative; height: 180px; }
.product-image { width: 100%; height: 100%; }
.category-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  background: rgba(0,0,0,0.4);
  backdrop-filter: blur(4px);
  color: #fff;
  padding: 2px 10px;
  border-radius: 20px;
  font-size: 12px;
}

.product-info { padding: 15px; text-align: left; }
.product-name { font-weight: bold; font-size: 15px; margin-bottom: 5px; height: 20px; overflow: hidden; }
.product-sku { font-size: 12px; color: #999; margin-bottom: 12px; }
.price-row { display: flex; justify-content: space-between; align-items: center; }
.price-val { color: #ff4d4f; font-size: 20px; font-weight: 900; }

.pagination-box { margin-top: 30px; display: flex; justify-content: center; }

/* 购买弹窗 */
.buy-box { text-align: left; }
.buy-target { display: flex; gap: 15px; margin-bottom: 20px; padding-bottom: 15px; border-bottom: 1px solid #eee; }
.buy-target img { width: 80px; height: 80px; border-radius: 4px; object-fit: cover; }
.num-picker { margin-bottom: 20px; display: flex; align-items: center; justify-content: space-between; }
.total { text-align: right; font-size: 16px; }
.total span { color: #f56c6c; font-size: 22px; font-weight: bold; }
</style>
