<template>
  <div class="admin-manage-container">
    <el-card class="search-card">
      <div class="flex-between">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="用户名">
            <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadData">查询</el-button>
          </el-form-item>
        </el-form>
        <el-button type="primary" icon="Plus" @click="handleAdd">新增管理员</el-button>
      </div>
    </el-card>

    <el-card class="mt-20">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="realName" label="真实姓名" />
        <el-table-column prop="role" label="角色">
          <template #default="scope">
            <el-tag :type="scope.row.role === 'SUPER_ADMIN' ? 'danger' : 'warning'">
              {{ scope.row.role === 'SUPER_ADMIN' ? '超级管理员' : '普通管理员' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="账号状态">
          <template #default="scope">
            <el-switch 
              v-model="scope.row.status" 
              :active-value="1" 
              :inactive-value="0" 
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button v-if="scope.row.role !== 'SUPER_ADMIN'" type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="scope.row.role !== 'SUPER_ADMIN'" type="warning" link @click="handleResetPwd(scope.row)">重置密码</el-button>
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑管理员' : '新增管理员'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" :disabled="!!form.id" placeholder="登录账号" />
        </el-form-item>
        <el-form-item v-if="!form.id" label="登录密码">
          <el-input v-model="form.password" type="password" show-password placeholder="默认 123456" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" placeholder="易于识别的名称" />
        </el-form-item>
        <el-form-item label="分配角色">
          <el-select v-model="form.role" style="width: 100%">
            <el-option label="普通管理员" value="ADMIN" />
            <el-option label="超级管理员" value="SUPER_ADMIN" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确定保存</el-button>
      </template>
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
const dialogVisible = ref(false)
const submitLoading = ref(false)

const searchForm = reactive({
  username: '',
  role: '', // 改为空，默认查所有，防止列表为空
  pageNum: 1,
  pageSize: 10
})

const form = reactive({
  id: null,
  username: '',
  password: '',
  realName: '',
  role: 'ADMIN'
})

const loadData = async () => {
  loading.value = true
  try {
    // 不再默认过滤，让列表能显示出当前账号或其他所有账号
    const res = await request.get('/user/page', { 
        params: { ...searchForm, role: searchForm.role || null } 
    })
    tableData.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  Object.assign(form, { id: null, username: '', password: '', realName: '', role: 'ADMIN' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleStatusChange = async (row) => {
  try {
    await request.put(`/user/${row.id}/status/${row.status}`)
    ElMessage.success('设置成功')
  } catch (err) {
    row.status = row.status === 1 ? 0 : 1
  }
}

const handleResetPwd = (row) => {
  ElMessageBox.confirm(`确定要将管理员 [${row.username}] 的密码重置为 123456 吗？`, '警告', {
    type: 'warning'
  }).then(async () => {
    await request.put(`/user/${row.id}/reset-password`)
    ElMessage.success('重置成功')
  })
}

const submitForm = async () => {
  submitLoading.value = true
  try {
    if (form.id) {
      await request.put('/user', form)
    } else {
      await request.post('/user/register', { ...form, password: form.password || '123456' })
    }
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadData()
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.admin-manage-container { padding: 10px; }
.flex-between { display: flex; justify-content: space-between; align-items: flex-start; }
.mt-20 { margin-top: 20px; }
.pagination-box { display: flex; justify-content: flex-end; }
</style>
