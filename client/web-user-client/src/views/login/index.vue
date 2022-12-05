<template>
    <div class="">
        <t-cell-group title="welcome">
            <t-input v-model="state.email" label="邮箱" placeholder="输入邮箱"/>
            <div>
                <t-input v-model="state.code" label="验证码" placeholder="输入验证码"/>
                <img :src="src" alt="" @click="getImageCode">
            </div>
            <div class="send-code">
                <t-input v-model="state.emailCode" placeholder="输入动态码"></t-input>
                <t-button size="small" theme="primary" block :disabled="false" @click="sendEmail">获取动态码</t-button>
            </div>
        </t-cell-group>

        <t-button size="small" theme="primary" block :disabled="false" @click="login">登录</t-button>
    </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { getCodeImage } from '@/network/codeImge.api'
import { sendLogInEmailCode } from '@/network/email.api';

const src = ref("");
const state = reactive({
    username: '',
    email: '',
    emailCode: '',
    code: '',
    password: '',
    codeKey:''
})


// 获取图片验证码
function getImageCode() {
    getCodeImage().then(({data: {data}}) => {
        state.codeKey = data;
        src.value = "http://39.103.233.82:10001/kaptcha/image?key=" + data;
    })
}
getImageCode()
function sendEmail() {
    sendLogInEmailCode({
        code:state.code,
        codeKey: state.codeKey,
        email: state.email
    }).then(({data:{code, msg}})=>{
        if (code === 200){
            // 发送成功
            // showNotify({ type: 'success', message: '通知内容' });
        }
    })
}

function login() {

}
</script>

<style scoped lang="scss">
.send-code {
    display: flex;
}
</style>