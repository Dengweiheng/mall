<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-left">
        <div class="logo-area">
          <el-icon :size="40" color="#409EFF"><ShoppingBag /></el-icon>
          <h1>X-ERP 系统</h1>
        </div>
        <div class="welcome-text">
          <p>欢迎使用电商 ERP 管理平台</p>
          <span>高效、便捷、专业的电商企业管家</span>
        </div>
      </div>
      <div class="login-right">
        <!-- 登录模式 -->
        <template v-if="mode === 'login'">
          <div class="form-title">账号登录</div>
          <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef">
            <el-form-item prop="username">
              <el-input 
                v-model="loginForm.username" 
                placeholder="用户名" 
                prefix-icon="User"
                size="large"
              />
            </el-form-item>
            <el-form-item prop="password">
              <el-input 
                v-model="loginForm.password" 
                type="password" 
                placeholder="密码" 
                prefix-icon="Lock"
                size="large"
                show-password
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            <div class="flex-between">
              <el-checkbox v-model="rememberMe">记住我</el-checkbox>
              <el-link type="primary" :underline="false">忘记密码？</el-link>
            </div>
            <el-button 
              type="primary" 
              class="login-btn" 
              :loading="loading" 
              @click="handleLogin"
              size="large"
            >
              立即登录
            </el-button>
            <div class="register-tip">
              还没有账号？ <el-link type="primary" :underline="false" @click="mode = 'register'">立即注册</el-link>
            </div>
          </el-form>
        </template>

        <!-- 注册模式 -->
        <template v-else>
          <div class="form-title">用户注册</div>
          <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef">
            <el-form-item prop="username">
              <el-input 
                v-model="registerForm.username" 
                placeholder="设置用户名" 
                prefix-icon="User"
                size="large"
              />
            </el-form-item>
            <el-form-item prop="password">
              <el-input 
                v-model="registerForm.password" 
                type="password" 
                placeholder="设置密码" 
                prefix-icon="Lock"
                size="large"
                show-password
              />
            </el-form-item>
            <el-form-item prop="realName">
              <el-input 
                v-model="registerForm.realName" 
                placeholder="真实姓名" 
                prefix-icon="Edit"
                size="large"
              />
            </el-form-item>
            <el-button 
              type="success" 
              class="login-btn" 
              :loading="loading" 
              @click="handleRegister"
              size="large"
            >
              提交注册
            </el-button>
            <div class="register-tip">
              已有账号？ <el-link type="primary" :underline="false" @click="mode = 'login'">立即登录</el-link>
            </div>
          </el-form>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const router = useRouter()
const mode = ref('login') // login or register
const loading = ref(false)
const rememberMe = ref(false)

// 登录相关
const loginFormRef = ref(null)
const loginForm = reactive({ username: '', password: '' })
const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// 注册相关
const registerFormRef = ref(null)
const registerForm = reactive({ username: '', password: '', realName: '' })
const registerRules = {
  username: [{ required: true, message: '请设置用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请设置密码', trigger: 'blur' }, { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const data = await request.post('/user/login', loginForm)
        localStorage.setItem('erp-user', JSON.stringify(data))
        ElMessage.success('登录成功，欢迎回来')
        router.push('/')
      } finally {
        loading.value = false
      }
    }
  })
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await request.post('/user/register', registerForm)
        ElMessage.success('注册成功，请登录')
        mode.value = 'login' // 注册成功切换到登录
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.login-box {
  width: 900px;
  height: 520px;
  display: flex;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 12px 32px rgba(0,0,0,0.1);
  overflow: hidden;
}

.login-left {
  flex: 1;
  background-color: #409EFF;
  color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 60px;
  background-image: linear-gradient(120deg, #1565c0, #1e88e5);
}

.logo-area {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 40px;
}

.logo-area h1 {
  font-size: 28px;
  margin: 0;
  font-weight: 700;
  letter-spacing: 1px;
}

.welcome-text p {
  font-size: 24px;
  font-weight: 500;
  margin-bottom: 10px;
}

.welcome-text span {
  font-size: 14px;
  opacity: 0.8;
}

.login-right {
  width: 450px;
  padding: 50px 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.form-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
}

.flex-between {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.login-btn {
  width: 100%;
  margin-bottom: 20px;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
}

.register-tip {
  text-align: center;
  font-size: 14px;
  color: #999;
}

:deep(.el-input__wrapper) {
  padding: 5px 15px;
}
</style>
