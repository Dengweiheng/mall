import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
        secure: false, // 禁用SSL校验
        // 如果后端接口带有 /api 前缀，后端已经通过 @RequestMapping 处理了，所以不需要 rewrite
        // rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
