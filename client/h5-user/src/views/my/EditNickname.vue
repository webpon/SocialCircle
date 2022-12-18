<template>
  <div>
    <NavBar>
      <template #right><span class="text-32px" @click="handlePetName">保存</span></template>
    </NavBar>
    <van-form ref="formRef">
      <van-field
        class="mt-20px"
        name="petName"
        v-model="formValue.petName"
        placeholder="请输入昵称（2-12字）"
        :rules="[
          {
            validator: validatePetMame(),
            trigger: 'onChange',
          },
        ]"
      />
    </van-form>

    <div class="note px-30px">
      <p>昵称支持2-12个中文字符或3-24个英文字符，</p>
      <p>符号仅支持”-“和”_“和”.“以及“·”</p>
    </div>
  </div>
</template>

<script setup lang="ts">
  import NavBar from './components/NavBar.vue';
  import {useUserStore, useUserStoreWidthOut} from '@/store/modules/user';
  import { onMounted, reactive, ref } from 'vue';
  import type { FormInstance } from 'vant';
  import { showToast } from 'vant';
  import {updateUserInfo} from "@/api/system/user";
  import {useRouter} from "vue-router";

  const userStore = useUserStore();

  const { petName } = userStore.getUserInfo;
  const formRef = ref<FormInstance>();

  const formValue = reactive({
    petName: '',
  });

  const validatePetMame = () => {
    return async (value: string) => {
      const pattern = /^[\u4E00-\u9FA5A-Za-z0-9-_.·]+$/;
      if (!pattern.test(value)) {
        return Promise.resolve('请输入正确内容');
      }
      if (value.length < 2 || value.length > 12) {
        return Promise.resolve('长度不符合');
      }
      return Promise.resolve(true);
    };
  };
  const router = useRouter()

  function handlePetName() {
    formRef.value
      ?.validate()
      .then(async () => {
        try {
          const formValue = formRef.value?.getValues();
          let petName = formValue?.petName +""
          updateUserInfo({petName}).then(() => {
              userStore.setUserInfo({petName})
              router.go(-1)
          })
        } finally {
          // after successful
        }
      })
      .catch(() => {
        console.error('验证失败');
      });
  }

  onMounted(() => {
    formValue.petName = petName;
  });
</script>

<style scoped lang="less">
  .note {
    margin-top: 15px;
    font-size: 25px;
    color: var(--van-text-color-2);
  }
</style>
