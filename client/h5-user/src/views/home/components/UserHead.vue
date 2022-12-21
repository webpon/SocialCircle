<template>
  <div class="head" @click="goto">
    <van-image
      round
      :width="headSize"
      :height="headSize"
      :src="info.headIcon"
    />
    <div>
      <div class="name">
        <div :style="{fontSize:`${fontSize}!important`, fontWeight}">
          {{info.petName}}
        </div>
        <div v-if="twoUserId !== 0">
          <van-icon name="play"/>
          <span
            :style="{fontSize, fontWeight}">
          {{twoInfo.petName}}
          </span>
        </div>
      </div>
      <div class="time">
        {{timeStr}}
      </div>
    </div>
    <van-tag class="tag" v-if="userId === 1" type="primary">官方</van-tag>
  </div>
</template>

<script setup lang="ts">


  import {getUserInfoById} from "@/api/system/user";
  import {ref} from "vue";
  import UserInfo from "@/type/UserInfo.type";
  import {getServerTime} from "@/api/other";
  import {useRouter} from "vue-router";

  interface por {
    userId: number;
    twoUserId: number;
    fontSize: number;
    headSize: number;
    fontWeight: number;
    time: number;
  }

  const {userId, twoUserId, fontSize, headSize, time} = withDefaults(
    defineProps<por>(), {
      twoUserId: 0,
      fontSize: 25,
      headSize: 40,
      time: 0,
      fontWeight: 700
    }
  );
  const info = ref<UserInfo>({})
  const twoInfo = ref<UserInfo>({})

  const emit = defineEmits(["updateName"]);

  // 获取用户
  getUserInfoById({userId}).then((data) => {
    info.value = data;
    emit("updateName", info.value.petName)
  }).catch(() => {
    info.value.petName = "用户已注销"
  })

  if (twoUserId != 0) {
    // 获取用户
    getUserInfoById({userId: twoUserId}).then((data) => {
      twoInfo.value.petName = data.petName;
    }).catch(() => {
      twoInfo.value.petName = ""
    })
  }
  const timeStr = ref("");

  getServerTime().then((t) => {
    let date = new Date(time);  // 参数需要毫秒数，所以这里将秒数乘于 1000
    const Y = date.getFullYear() + '-';
    const M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    const D = date.getDate() + ' ';
    const now = new Date(t as unknown as number);
    const Y1 = now.getFullYear() + '-';
    const D1 = now.getDate() + ' ';
    if (Y !== Y1) {
      timeStr.value += Y;
    }
    if (D !== D1) {
      timeStr.value += M;
      timeStr.value += D;
    }
    timeStr.value += (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    const minutes = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes());
    timeStr.value += minutes
  })
  const router = useRouter()

  const goto = ()=>{
    router.push({
      name: 'userInfo',
      params: {id: userId}
    })
  }

</script>

<style scoped lang="less">
  .head {
    display: flex;
    align-items: center;
    margin: 5px !important;

    div {
      margin: 0 5px;

      .name{
        display: flex;
      }
      .time {
        font-size: 20px;
      }
    }

    .tag {
      position: relative;
      top: -15px;

      > span {
        margin: 5px 10px;
        font-size: 30px;
      }
    }
  }
</style>
