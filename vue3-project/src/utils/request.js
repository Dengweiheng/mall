import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
    baseURL: '/api', // 使用 Vite 代理
    timeout: 5000
})

// 请求拦截器
request.interceptors.request.use(config => {
    // 可以在这里添加 token
    const user = JSON.parse(localStorage.getItem('erp-user') || '{}')
    if (user.id) {
        config.headers['userId'] = user.id // 简单鉴权，实际生产用 Token
    }
    return config
}, error => {
    return Promise.reject(error)
})

// 响应拦截器
request.interceptors.response.use(response => {
    const res = response.data
    if (res.code === 200) {
        return res.data
    } else {
        ElMessage.error(res.message || '系统错误')
        return Promise.reject(new Error(res.message || 'Error'))
    }
}, error => {
    ElMessage.error(error.message || '网络连接失败')
    return Promise.reject(error)
})

export default request
