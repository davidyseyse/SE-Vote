import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    watch: {
      usePolling: true,
    },
    proxy: {
    '/api/': {
      target: 'http://140.115.59.213:8080',
      changeOrigin: true,
      // 不 rewrite，完整轉發
      }
    }
  },
})
