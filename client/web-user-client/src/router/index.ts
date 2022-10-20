import { createRouter, createWebHistory } from "vue-router";
import Login from "@/views/login/index.vue";
import Home from "@/views/home/index.vue";
import Register from "@/views/register/index.vue";
const routes: any = [
    {
        path: '/home',
        component: Home
    },
    {
        path: '/login',
        component: Login
    },
    {
        path: '/register',
        component: Register
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router