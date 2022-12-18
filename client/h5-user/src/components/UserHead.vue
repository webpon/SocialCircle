<template>
  <div>
    <van-image
      round
      :width="headSize"
      :height="headSize"
      :src="info.headIcon"
    />
    <span :style="{fontSize:`${fontSize}!important`, fontWeight}">
      {{info.petName}}
    </span>
    <div v-if="twoUserId !== 0">
      <van-icon name="play" />
      <span
        :style="{fontSize, fontWeight}">
        {{twoInfo.petName}}
      </span>
    </div>
    <van-tag v-if="userId === 1" type="primary">官方</van-tag>
  </div>
</template>

<script setup lang="ts">


  import {getUserInfoById} from "@/api/system/user";
  import { ref } from "vue";
  import UserInfo from "@/type/UserInfo.type";

  interface por {
    userId: number;
    twoUserId: number;
    fontSize: number;
    headSize: number;
    fontWeight: number;
  }

  const {userId, twoUserId, fontSize, headSize} = withDefaults(
    defineProps<por>(),{
      twoUserId: 0,
      fontSize:25,
      headSize:40,
      fontWeight: 700
    }
  );
  const info = ref<UserInfo>({})
  const twoInfo = ref<UserInfo>({})

  const emit = defineEmits(["updateName"]);

  // 获取用户
  getUserInfoById({userId}).then((data)=>{
    info.value = data;
    emit("updateName", info.value.petName)
  }).catch(()=>{
      info.value.petName= "用户已注销"
  })

  if (twoUserId != 0) {
    // 获取用户
    getUserInfoById({userId:twoUserId}).then((data) => {
      twoInfo.value.petName = data.petName;
    }).catch(() => {
      twoInfo.value.petName = ""
    })
  }
</script>

<style scoped lang="less">
  div {
  display: flex;
  align-items: center;
  margin: 5px !important;

  > span {
    margin: 0 10px;
    font-size: 30px;
  }
}
</style>
