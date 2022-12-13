<template>
  <div>
    <van-image
      round
      width="40"
      height="40"
      :src="info.headIcon"
    />
    <span>{{info.petName}}</span>
    <van-tag v-if="userId === 1" type="primary">官方</van-tag>
  </div>
</template>

<script setup lang="ts">


  import {getUserInfoById} from "@/api/system/user";
  import { ref } from "vue";
  import UserInfo from "@/type/UserInfo.type";

  interface por {
    userId: number
  }

  const {userId} = defineProps<por>();

  const info = ref<UserInfo>({})

  // 获取用户
  getUserInfoById({userId}).then((data)=>{
    info.value = data;
  })
</script>

<style scoped lang="less">
div {
  display: flex;
  align-items: center;
  margin: 5px !important;

  > span {
    margin: 0 10px;
    font-size: 30px;
    font-weight: 700;
  }
}
</style>
