import { createApp } from 'vue'
import TDesign from 'tdesign-mobile-vue';
import router from './router/index'
import './style.css'
import App from './App.vue'
// import http from '@/network/index';


const app = createApp(App)
app.use(router)
// 第三方依赖
app.use(TDesign);
// app.config.globalProperties.$http = http;  //配置axios的全局引用
app.mount('#app')
