<template>
  <van-uploader
    :max-size="700 * 1024"
    :max-count="1"
    :before-read="beforeRead"
    :after-read="afterRead"
    accept="image/*"
  >
    <template #default>
      <slot name="default"></slot>
    </template>
  </van-uploader>
</template>

<script setup lang="ts">
  import { showFailToast } from 'vant';
  import {uploaderFile} from "@/api/oss";
  import {updateUserInfo} from "@/api/system/user";
  import {useUserStore} from "@/store/modules/user";
  const userStore = useUserStore();

  function beforeRead(file) {
    if (file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/jpg') {
      uploaderFile(file).then((headIcon)=>{
        updateUserInfo({headIcon}).then((code) =>{
          if (code ){
            userStore.setUserInfo({headIcon})
          }
        })

      })
      return true;
    }
    showFailToast('请上传正确格式的图片');
    return false;
  }

  function afterRead(file) {
    // 这里写上传逻辑
  }
</script>

<style scoped lang="less"></style>
