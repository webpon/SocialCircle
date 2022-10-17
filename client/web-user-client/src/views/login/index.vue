<template>
    <div class="">
        <t-cell-group title="welcome">
            <t-input v-model="state.username" label="邮箱" placeholder="输入邮箱">
            </t-input>
            <div class="send-code">
                <t-input v-model="state.code" placeholder="输入验证码"></t-input>
                <t-button size="small" theme="primary" block :disabled="false">获取验证码</t-button>
            </div>
        </t-cell-group>

        <t-button size="small" theme="primary" block :disabled="false" @click="login">登录</t-button>
    </div>
</template>

<script setup lang="ts">
import { reactive, onMounted, getCurrentInstance, ComponentInternalInstance } from 'vue'
import { getTest } from '@/network/apis'
const { proxy } = getCurrentInstance() as ComponentInternalInstance
onMounted(async () => {
    const data = await getTest()
    console.log(data);
})
const state = reactive({
    username: '',
    email: '',
    code: '',
    password: ''
})

function login() {
    proxy?.$router.push('/home')
}
</script>

<style scoped lang="scss">
.send-code {
    display: flex;
}
</style>