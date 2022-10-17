import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  //全局引入
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `
        @import '@/styles/var.scss';
        @import '@/styles/mixins.scss';
      `,
      }
    }
  },
})
