<template>
  <van-form ref="formRef" v-if="getShow" class="flex flex-col" @submit="handleRegister">
    <van-cell-group inset class="enter-y !mx-0 !mb-60px">
      <van-field class="enter-y items-center !rounded-md" v-model="formData.petName" name="username" placeholder="用户名"
        :rules="getFormRules.petName">
        <template #left-icon>
          <Icon>
            <UserOutlined />
          </Icon>
        </template>
      </van-field>

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
          <img class="w-200px" :src="`http://39.103.233.82:10001/kaptcha/image?key=${key}`" />
        </template>
      </van-field>
      <van-field class="enter-y items-center !rounded-md" v-model="formData.emailCode" center clearable
        placeholder="请输入短信验证码" :rules="getFormRules.sms">
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

      <van-field class="enter-y items-center !rounded-md" v-model="formData.password"
        :type="switchPassType ? 'password' : 'text'" name="password" placeholder="密码" :rules="getFormRules.password"
        @click-right-icon="switchPassType = !switchPassType">
        <template #left-icon>
          <Icon>
            <LockOutlined />
          </Icon>
        </template>
        <template #right-icon>
          <Icon v-if="switchPassType">
            <EyeInvisibleOutlined />
          </Icon>
          <Icon v-else>
            <EyeOutlined />
          </Icon>
        </template>
      </van-field>

      <van-field class="enter-y items-center !rounded-md" v-model="formData.confirmPassword"
        :type="switchConfirmPassType ? 'password' : 'text'" name="confirmPassword" placeholder="确认密码"
        :rules="getFormRules.confirmPassword" @click-right-icon="switchConfirmPassType = !switchConfirmPassType">
        <template #left-icon>
          <Icon>
            <LockOutlined />
          </Icon>
        </template>
        <template #right-icon>
          <Icon v-if="switchConfirmPassType">
            <EyeInvisibleOutlined />
          </Icon>
          <Icon v-else>
            <EyeOutlined />
          </Icon>
        </template>
      </van-field>

      <van-field name="policy" class="enter-y items-center px-1 !rounded-md" :rules="getFormRules.policy">
        <template #input>
          <van-checkbox v-model="formData.policy" icon-size="14px" shape="square">
            我同意 xxx 隐私政策
          </van-checkbox>
        </template>
      </van-field>
    </van-cell-group>

    <van-button class="enter-y !mb-25px !rounded-md" type="primary" block native-type="submit" :loading="loading">
      注 册
    </van-button>

    <van-button class="enter-y !mb-150px !rounded-md" plain type="primary" block @click="handleBackLogin">
      返 回
    </van-button>
  </van-form>
</template>

<script setup lang="ts">
import { computed, reactive, ref, unref, onMounted } from 'vue';
import type { FormInstance } from 'vant';
import { showSuccessToast } from 'vant';
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
import { LoginStateEnum, useLoginState, useFormRules } from './useLogin';
import { getCaptchaKey, getCaptcha, sendEmailCode as sendEmailCodeApi } from '@/api/system/user';

const { handleBackLogin, getLoginState } = useLoginState();
const getShow = computed(() => unref(getLoginState) === LoginStateEnum.REGISTER);

const loading = ref(false);
const formRef = ref<FormInstance>();
const captchaImg = ref('')

const formData = reactive({
  petName: '',
  email: '',
  check: '',
  emailCode: '',
  password: '',
  confirmPassword: '',
  policy: false,
});

const { getFormRules } = useFormRules(formData);

const switchPassType = ref(true);
const switchConfirmPassType = ref(true);
const key = ref('')
onMounted(async () => {
  key.value = await getCaptchaKey() || ''
  // console.log(key);
  // captchaImg.value = await getCaptcha(key)
})


// 发送邮箱验证码
async function sendEmailCode() {
  const data = await sendEmailCodeApi({
    email: '2249096563@qq.com',
    code: formData.check,
    key: key.value
  })
}

function handleRegister() {
  formRef.value
    ?.validate()
    .then(async () => {
      try {
        loading.value = true;
        // getCaptcha()
        // do something
        showSuccessToast({
          message: '注册成功！',
          position: 'top',
        })
      } finally {
        loading.value = false;
      }
    })
    .catch(() => {
      console.error('验证失败');
    });
}
</script>

<style scoped lang="less">

</style>
