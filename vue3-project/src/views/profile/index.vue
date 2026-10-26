<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="user-card" shadow="hover">
          <div class="user-header">
            <el-upload
              class="avatar-uploader"
              action="/api/file/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
            >
              <el-avatar :size="100" :src="userForm.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
              <div class="upload-tip">点击修改头像</div>
            </el-upload>
            <h2 class="mt-20">{{ userForm.realName || userForm.username }}</h2>
            <el-tag :type="roleType" class="mt-10">{{ roleName }}</el-tag>
          </div>
          <div class="user-info-list mt-20">
            <div class="info-item">
              <el-icon><User /></el-icon>
              <span>账号：{{ userForm.username }}</span>
            </div>
            <div class="info-item">
              <el-icon><Phone /></el-icon>
              <span>手机：{{ userForm.phone || '未绑定' }}</span>
            </div>
            <div class="info-item">
              <el-icon><Message /></el-icon>
              <span>邮箱：{{ userForm.email || '未绑定' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="hover">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="basic">
              <el-form :model="userForm" label-width="100px" class="mt-20">
                <el-form-item label="真实姓名">
                  <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
                </el-form-item>
                <el-form-item label="联系电话">
                  <el-input v-model="userForm.phone" placeholder="请输入手机号" />
                </el-form-item>
                <el-form-item label="电子邮箱">
                  <el-input v-model="userForm.email" placeholder="请输入邮箱" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" :loading="saveLoading" @click="saveProfile">保存修改</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="账号安全" name="security">
              <el-form :model="pwdForm" :rules="pwdRules" ref="pwdFormRef" label-width="100px" class="mt-20">
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input v-model="pwdForm.oldPassword" type="password" show-password placeholder="请输入当前密码" />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="请输入新密码" />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input v-model="pwdForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
                </el-form-item>
                <el-form-item>
                  <el-button type="danger" :loading="pwdLoading" @click="updatePwd">修改密码</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const activeTab = ref('basic')
const saveLoading = ref(false)
const pwdLoading = ref(false)
const pwdFormRef = ref(null)

const cachedUser = JSON.parse(localStorage.getItem('erp-user') || '{}')
const userForm = reactive({
  id: cachedUser.id,
  username: cachedUser.username,
  realName: cachedUser.realName || '',
  phone: cachedUser.phone || '',
  email: cachedUser.email || '',
  avatar: cachedUser.avatar || '',
  role: cachedUser.role
})

const pwdForm = reactive({
  id: cachedUser.id,
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }, { min: 6, message: '长度不能少于6位', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== pwdForm.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const roleName = computed(() => {
  if (userForm.role === 'SUPER_ADMIN') return '超级管理员'
  if (userForm.role === 'ADMIN') return '管理员'
  return '普通用户'
})

const roleType = computed(() => {
  if (userForm.role === 'SUPER_ADMIN') return 'danger'
  if (userForm.role === 'ADMIN') return 'warning'
  return 'info'
})

const loadProfile = async () => {
  try {
    const data = await request.get(`/user/${userForm.id}`)
    Object.assign(userForm, data)
  } catch (err) {
    console.error(err)
  }
}

const saveProfile = async () => {
  saveLoading.value = true
  try {
    await request.put('/user', userForm)
    ElMessage.success('资料更新成功')
    localStorage.setItem('erp-user', JSON.stringify(userForm))
  } finally {
    saveLoading.value = false
  }
}

const updatePwd = async () => {
  if (!pwdFormRef.value) return
  await pwdFormRef.value.validate(async (valid) => {
    if (valid) {
      pwdLoading.value = true
      try {
        await request.put('/user/password', {
          id: pwdForm.id,
          oldPassword: pwdForm.oldPassword,
          newPassword: pwdForm.newPassword
        })
        ElMessage.success('密码修改成功，请重新登录')
        localStorage.removeItem('erp-user')
        window.location.href = '/login'
      } finally {
        pwdLoading.value = false
      }
    }
  })
}

const handleAvatarSuccess = (res) => {
  if (res.code === 200) {
    userForm.avatar = res.data
    ElMessage.success('头像上传成功，请保存修改以使全局生效')
  } else {
    ElMessage.error(res.message || '上传失败')
  }
}

onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.profile-container {
  padding: 10px;
}

.user-card {
  text-align: center;
  padding: 30px 20px;
}

.user-header {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-uploader {
  cursor: pointer;
  position: relative;
}

.upload-tip {
  position: absolute;
  bottom: 0;
  width: 100%;
  background: rgba(0,0,0,0.5);
  color: #fff;
  font-size: 12px;
  padding: 5px 0;
  opacity: 0;
  transition: opacity 0.3s;
  border-bottom-left-radius: 50% 100%;
  border-bottom-right-radius: 50% 100%;
}

.avatar-uploader:hover .upload-tip {
  opacity: 1;
}

.user-info-list {
  text-align: left;
  border-top: 1px solid #f0f0f0;
  padding-top: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
  color: #606266;
  font-size: 14px;
}

.mt-10 { margin-top: 10px; }
.mt-20 { margin-top: 20px; }
</style>
