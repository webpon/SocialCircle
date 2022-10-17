import { createRouter, createWebHistory } from "vue-router";
import login from "@/views/login/index.vue";
import home from "@/views/home/index.vue";
const routes: any = [
    {
        path: '/home',
        component: home
    },
    {
        path: '/login',
        component: login
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router