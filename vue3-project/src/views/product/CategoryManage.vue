<template>
  <div class="category-manage-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>分类管理</span>
          <el-button type="primary" @click="handleAdd">新增分类</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column prop="id" label="ID" width="100" />
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="createTime" label="创建时间">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="分类名称">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增分类')
const form = reactive({
  id: null,
  name: ''
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/category/all')
    tableData.value = res
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增分类'
  form.id = null
  form.name = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑分类'
  form.id = row.id
  form.name = row.name
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.name) {
    ElMessage.warning('请输入分类名称')
    return
  }
  if (form.id) {
    await request.put('/category', form)
    ElMessage.success('更新成功')
  } else {
    await request.post('/category', form)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  loadData()
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除分类【${row.name}】吗？删除后该分类下的商品将失去分类关联。`, '警告', {
    type: 'warning'
  }).then(async () => {
    await request.delete(`/category/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  })
}

const formatTime = (time) => {
  if (!time) return '-'
  return time.replace('T', ' ').substring(0, 19)
}

onMounted(loadData)
</script>

<style scoped>
.category-manage-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
