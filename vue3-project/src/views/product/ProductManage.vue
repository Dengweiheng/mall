<template>
  <div class="product-manage-container">
    <el-card class="search-card">
      <div class="flex-between">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="商品名称">
            <el-input v-model="searchForm.name" placeholder="名称" clearable />
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="searchForm.category" placeholder="全部" clearable style="width: 120px">
              <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.name" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadData">查询</el-button>
          </el-form-item>
        </el-form>
        <el-button type="success" icon="Plus" @click="handleAdd">新增商品</el-button>
      </div>
    </el-card>

    <el-card class="mt-20">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="image" label="图片" width="80">
          <template #default="scope">
            <el-image 
              style="width: 40px; height: 40px; border-radius: 4px" 
              :src="scope.row.image || 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'" 
              fit="cover" 
            />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="sku" label="SKU 编码" width="120" />
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="price" label="零售价" width="100">
           <template #default="scope"><span class="price">￥{{ scope.row.price }}</span></template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.stock < 10 ? 'danger' : 'success'">{{ scope.row.stock }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-switch 
              v-model="scope.row.status" 
              :active-value="1" 
              :inactive-value="0" 
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除吗？不可撤销" @confirm="handleDelete(scope.row)">
              <template #reference>
                <el-button type="danger" link>删除</el-button>
              </template>
            </el-popconfirm>
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

    <!-- 编辑/新增 弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑商品' : '新增商品'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="SKU 编码">
          <el-input v-model="form.sku" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="商品分类">
          <el-select v-model="form.category" style="width: 100%" @change="onCategoryChange">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="销售价格">
          <el-input-number v-model="form.price" :min="0.1" style="width: 100%" />
        </el-form-item>
        <el-form-item v-if="!form.id" label="初始库存">
          <el-input-number v-model="form.stock" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="商品图片">
          <el-upload
            class="avatar-uploader"
            action="/api/file/upload"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
          >
            <img v-if="form.image" :src="form.image" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const categories = ref([])

const loadCategories = async () => {
  const res = await request.get('/category/all')
  categories.value = res
}

const onCategoryChange = (val) => {
  const selected = categories.value.find(c => c.name === val)
  if (selected) {
    form.categoryId = selected.id
  }
}

const searchForm = reactive({
  name: '',
  category: '',
  pageNum: 1,
  pageSize: 10
})

const form = reactive({
  id: null,
  name: '',
  sku: '',
  categoryId: null,
  category: '',
  price: 0,
  stock: 0,
  image: '',
  status: 1
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/product/page', { params: searchForm })
    tableData.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  Object.assign(form, { id: null, name: '', sku: '', categoryId: null, category: '', price: 0, stock: 0, image: '', status: 1 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleStatusChange = async (row) => {
  try {
    await request.put(`/product/${row.id}/status/${row.status}`)
    ElMessage.success('状态更新成功')
  } catch (err) {
    row.status = row.status === 1 ? 0 : 1 // 还原
  }
}

const handleDelete = async (row) => {
  await request.delete(`/product/${row.id}`)
  ElMessage.success('删除成功')
  loadData()
}

const handleUploadSuccess = (res) => {
  if (res.code === 200) {
    form.image = res.data
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(res.message || '上传失败')
  }
}

const submitForm = async () => {
  if (form.id) {
    await request.put('/product', form)
  } else {
    await request.post('/product', form)
  }
  ElMessage.success('操作成功')
  dialogVisible.value = false
  loadData()
}

onMounted(() => {
  loadCategories()
  loadData()
})
</script>

<style scoped>
.flex-between { display: flex; justify-content: space-between; align-items: flex-start; }
.mt-20 { margin-top: 20px; }
.price { color: #f56c6c; font-weight: bold; }
.pagination-box { display: flex; justify-content: flex-end; }

.avatar-uploader {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  width: 120px;
  height: 120px;
}

.avatar-uploader:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}

.avatar {
  width: 120px;
  height: 120px;
  display: block;
  object-fit: cover;
}
</style>
