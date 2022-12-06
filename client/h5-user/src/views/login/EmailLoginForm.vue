<template>
  <van-form ref="formRef" v-if="getShow" class="flex flex-col items-center" @submit="handleSubmit">

    <van-field class="enter-y items-center !rounded-md" v-model="formData.email" name="password" placeholder="邮箱"
               :rules="getFormRules.mobile">
      <template #left-icon>
        <Icon>
          <MailOutlined />
        </Icon>
      </template>
    </van-field>
    <van-field class="enter-y items-center !rounded-md" v-model="formData.check" name="password"
               placeholder="请输入图片验证码" :rules="getFormRules.check">
      <template #left-icon>
        <Icon>
          <ShieldCheckmarkOutline />
        </Icon>
      </template>
      <template #button>
        <img class="w-200px" :src="`http://39.103.233.82:10001/kaptcha/image?key=${key}`"  @click="updateCaptchaKey"/>
      </template>
    </van-field>
    <van-field class="enter-y items-center !rounded-md" v-model="formData.emailCode" center clearable
               placeholder="请输入邮箱验证码" :rules="getFormRules.sms">
      <template #left-icon>
        <Icon>
          <EditOutlined />
        </Icon>
      </template>
      <template #button>
        <van-button size="small" type="primary" :disabled="(formData.check.length !== 4)"
                    @click="sendEmailCode">发送验证码</van-button>
      </template>
    </van-field>

    <div class="enter-y w-full px-5px flex justify-between mb-100px">
        <a class="!text-25px" @click="setLoginState(LoginStateEnum.LOGIN)">密码登录</a>
    </div>

    <van-button
      class="enter-y !rounded-md !mb-25px"
      type="primary"
      block
      native-type="submit"
      :loading="loading"
    >
      登 录
    </van-button>
    <van-button
      class="enter-y !mb-25 !rounded-md"
      plain
      type="primary"
      block
      @click="setLoginState(LoginStateEnum.REGISTER)"
    >
      注 册
    </van-button>
  </van-form>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, unref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { showFailToast, showLoadingToast, showSuccessToast } from 'vant';
import type { FormInstance } from 'vant';
import { Icon } from '@vicons/utils';
import {
  UserOutlined,
  MailOutlined,
  EditOutlined,
  LockOutlined,
  EyeOutlined,
  EyeInvisibleOutlined,
} from '@vicons/antd';
import { ShieldCheckmarkOutline } from '@vicons/ionicons5'

import { useUserStore } from '@/store/modules/user';
import { ResultEnum } from '@/enums/httpEnum';
import { PageEnum } from '@/enums/pageEnum';
import { LoginStateEnum, useLoginState, useFormRules } from './useLogin';
import {getCaptchaKey, logInEmailCode as sendEmailCodeApi} from "@/api/system/user";

const { setLoginState, getLoginState } = useLoginState();
const { getFormRules } = useFormRules();
const userStore = useUserStore();
const router = useRouter();
const route = useRoute();

const formRef = ref<FormInstance>();
const loading = ref(false);
const rememberMe = ref(false);
const switchPassType = ref(true);
const formData = reactive({
  email: '',
  check: '',
  emailCode: '',
  policy: false
});

const getShow = computed(() => unref(getLoginState) === LoginStateEnum.EMAIL_LOGIN);

const key = ref('')
onMounted(async () => {
  key.value = await getCaptchaKey() || ''
})
function updateCaptchaKey(){
  getCaptchaKey().then((data)=>{
    key.value = data || ''
    formData.check = '';
  })
}
// 发送邮箱验证码
async function sendEmailCode() {
  const {msg} = await sendEmailCodeApi({
    email: formData.email,
    code: formData.check,
    codeKey: key.value
  })
  showFailToast(msg);
}

function handleSubmit() {
  formRef.value
    ?.validate()
    .then(async () => {
      try {
        loading.value = true;
        showLoadingToast('登录中...');
        const { code, msg } = await userStore.LoginByEmailCode({
          email: formData.email,
          emailCode: formData.emailCode
        });

        if (code == ResultEnum.SUCCESS) {
          const toPath = decodeURIComponent((route.query?.redirect || '/') as string);
          showSuccessToast('登录成功，即将进入系统');
          if (route.name === PageEnum.BASE_LOGIN_NAME) {
            router.replace('/');
          } else router.replace(toPath);
        } else {
          showFailToast(msg || '登录失败');
        }
      } finally {
        loading.value = false;
      }
    })
    .catch(() => {
      console.error('验证失败');
    });
}

onMounted(() => {});
</script>

<style scoped lang="less"></style>
