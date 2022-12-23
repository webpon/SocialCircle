<template>
  <div class="user">
    <header>
      <van-icon name="arrow-left" size="20" class="icon" @click="retreat"/>
      <div>用户信息</div>
    </header>
    <nav>
      <div class="info">
        <van-image
          class="head"
          round
          :width="60"
          :height="60"
          :src="info.headIcon"
        />
        <div class="name">
          {{info.remarks || info.petName}}
          <span v-if="info.gender === 2" class="sex">
            <svg t="1671609708001" class="icon" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="2768"
                 width="20" height="20"><path
              d="M510.887666 512m-446.708971 0a446.708971 446.708971 0 1 0 893.417942 0 446.708971 446.708971 0 1 0-893.417942 0Z"
              fill="#FFFFFF" p-id="2769"></path><path
              d="M510.87948 578.902736c-123.673717 0-224.282113-100.607372-224.282113-224.282113s100.607372-224.282113 224.282113-224.282113 224.282113 100.607372 224.282112 224.282113-100.608396 224.282113-224.282112 224.282113z m0-364.80559c-77.486792 0-140.523477 63.036685-140.523477 140.523477s63.036685 140.523477 140.523477 140.523477 140.523477-63.036685 140.523477-140.523477-63.036685-140.523477-140.523477-140.523477z"
              fill="#FF3EC9" p-id="2770"></path><path
              d="M510.87948 896.635217c-23.12058 0-41.878806-18.744923-41.878806-41.878806V537.02393c0-23.133883 18.758226-41.878806 41.878806-41.878806s41.878806 18.744923 41.878806 41.878806v317.732481c0 23.133883-18.758226 41.878806-41.878806 41.878806z"
              fill="#FF3EC9" p-id="2771"></path><path
              d="M669.752884 737.762837H352.033705c-23.12058 0-41.878806-18.744923-41.878806-41.878806s18.758226-41.878806 41.878806-41.878807h317.719179c23.12058 0 41.878806 18.744923 41.878806 41.878807s-18.758226 41.878806-41.878806 41.878806z"
              fill="#FF3EC9" p-id="2772"></path></svg>

          </span>
          <span v-else class="sex">
            <svg t="1671609754563" class="icon" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="4232"
                 width="20" height="20"><path
              d="M511.843434 512m-446.708971 0a446.708971 446.708971 0 1 0 893.417942 0 446.708971 446.708971 0 1 0-893.417942 0Z"
              fill="#FFFFFF" p-id="4233"></path><path
              d="M424.111301 818.825061c-59.328223 0-115.140367-23.107277-157.101038-65.081251-86.620823-86.620823-86.620823-227.581252 0-314.215378 41.960671-41.973974 97.771791-65.081251 157.101038-65.081251 59.355853 0 115.140367 23.12058 157.101037 65.081251 41.960671 41.973974 65.081251 97.771791 65.081251 157.11434s-23.12058 115.140367-65.081251 157.101038-97.745185 65.081251-157.101037 65.081251z m0-360.620268c-36.97103 0-71.733765 14.409175-97.881285 40.543392-53.957913 53.984518-53.957913 141.804656 0 195.775872 26.14752 26.14752 60.910255 40.543392 97.881285 40.543391s71.733765-14.395872 97.881284-40.543391c26.14752-26.14752 40.543392-60.910255 40.543392-97.881285s-14.395872-71.733765-40.543392-97.894587c-26.146497-26.14752-60.909232-40.543392-97.881284-40.543392z"
              fill="#75B9EB" p-id="4234"></path><path
              d="M551.602973 511.016603c-10.715039 0-21.430078-4.090155-29.609365-12.269442-16.358573-16.358573-16.358573-42.874483 0-59.219753L672.577209 288.943808h-42.833551c-23.12058 0-41.878806-18.744923-41.878806-41.878806s18.758226-41.878806 41.878806-41.878806h143.958716c16.931624 0 32.200376 10.210549 38.689161 25.847691 6.488785 15.650445 2.889817 33.67189-9.078773 45.641503L581.212338 498.747161c-8.179286 8.179286-18.894326 12.269441-29.609365 12.269442z"
              fill="#75B9EB" p-id="4235"></path><path
              d="M773.703397 288.943808h-143.958716c-23.12058 0-41.878806-18.744923-41.878806-41.878806s18.758226-41.878806 41.878806-41.878806h143.958716c23.12058 0 41.878806 18.744923 41.878806 41.878806s-18.758226 41.878806-41.878806 41.878806z"
              fill="#75B9EB" p-id="4236"></path><path
              d="M779.864724 439.050548c-23.12058 0-41.878806-18.744923-41.878806-41.878806V253.226329c0-23.133883 18.758226-41.878806 41.878806-41.878807s41.878806 18.744923 41.878806 41.878807v143.945413c0 23.133883-18.758226 41.878806-41.878806 41.878806z"
              fill="#75B9EB" p-id="4237"></path><path
              d="M779.864724 439.050548c-23.12058 0-41.878806-18.744923-41.878806-41.878806V253.226329c0-23.133883 18.758226-41.878806 41.878806-41.878807s41.878806 18.744923 41.878806 41.878807v143.945413c0 23.133883-18.758226 41.878806-41.878806 41.878806z"
              fill="#75B9EB" p-id="4238"></path></svg>
          </span>
        </div>
        <div class="other">
          <div class="id">ID:{{info.accountId}}</div>
          <div class="age">{{age}} 岁</div>
          <div class="birthday">{{info.birthday || "生日:未知"}}</div>
          <div class="region">{{info.region || "地区:未知"}}</div>
        </div>
        <div class="handle">
          <div @click="concern">
            <van-button v-if="showCon" type="primary" class="but cancel">取消关注</van-button>
            <van-button v-else-if="info.fens" type="primary" class="but">回关</van-button>
            <van-button v-else type="primary" class="but">关注</van-button>
          </div>
          <van-button type="primary" class="but" >发信息</van-button>
        </div>
      </div>
      <van-tabs
        v-model:active="active"
        swipeable
        :line-width="20"
        shrink
        sticky
        class="relative"
        background="#eee"
      >
        <van-tab title="动态" :title-style="{ fontSize: '17px', margin: '0 5px' }">
          <van-pull-refresh v-model="loadingUp" @refresh="onRefresh">
            <van-list
              v-model:loading="loading"
              :finished="finished"
              finished-text="没有更多了"
              @load="onLoad"
            >
              <Dynamic v-for="item in dynamics" :dynamic="item.dynamic" :images="item.images"
                       @deleteDyn="deleteDyn" :key="item.dynamic.id"/>
            </van-list>
          </van-pull-refresh>

        </van-tab>
        <van-tab title="工作经历" :title-style="{ fontSize: '17px', margin: '0 5px' }">
        </van-tab>
      </van-tabs>
    </nav>
  </div>
</template>

<script setup lang="ts">
  import {useRoute, useRouter} from "vue-router";
  import {onBeforeMount, ref, watchEffect} from "vue";
  import UserInfo from "@/type/UserInfo.type";
  import {getUserInfoById} from "@/api/system/user";
  import {getServerTime} from "@/api/other";
  import {concern as concernApi} from "@/api/friend";
  import {showFailToast} from "vant";
  import DynamicVM from "@/type/DynamicVM";
  import {getDynamicByRecommended, getDynamicByUserId} from "@/api/dynamic";
  import useDyanmic from "@/views/home/pages/useDyanmic";

  const route = useRoute()
  const {id} = route.params;
  const info = ref<UserInfo>({})
  const age = ref(0)
  const loading = ref(false);
  const finished = ref(false);
  const dynamics = ref<Array<DynamicVM>>([])
  const p = ref(1);
  const showCon = ref(false)
  const active = ref(0);
  const loadingUp = ref(false);
  const router = useRouter();

  getUserInfoById({userId: id as unknown as number}).then((data) => {
    info.value = data as UserInfo;
  }).catch(() => {
    info.value.petName = "用户已注销"
  })

  watchEffect(async () => {
    const {birthday,concern,friend} = info.value;
    if (birthday != null) {
      const t = await getServerTime() as unknown as number;
      let birthDayTime = new Date(birthday).getTime();
      age.value = Math.ceil((t - birthDayTime) / 31536000000);
    }
    showCon.value = (friend || concern) as boolean;
  });

  const concern = async ()=>{
    const userId = info.value.id as number;
    const {code,msg} = await concernApi(userId);
    showFailToast(msg);
    if (code !== 200){
      return ;
    }
    const {friend} = info.value;
    showCon.value = !showCon.value;
    if (friend){
      info.value.fens = true;
    }

  }


  function getDy(p: number) {
    loading.value = true;
    getDynamicByUserId(id,p).then((data) => {
      dynamics.value.push(...data)
      finished.value = data.length !== 10;
      loading.value = false;
    })
  }
  const {deleteDyn,onLoad,onRefresh} = useDyanmic(dynamics, p, getDy,loadingUp);

  const retreat = ()=>router.go(-1)
</script>

<style scoped lang="less">
  .user {
    background-color: #eee;

    header {
      background-color: #ffffff;
      display: flex;
      height: 70px;
      line-height: 70px;
      position: fixed;
      top: 0;
      width: 100%;
      z-index: 99;

      .icon {
        width: 60px;
        margin: auto 0;
      }

      div {
        margin: auto;
        font-size: 30px;
        font-weight: 600;
      }
    }

    nav {
      margin-top: 70px;
      border: transparent 1px;
      display: flex;
      flex-direction: column;

      .info {
        padding: 10px;
        margin: 160px auto 30px;
        border-radius: 20px;
        background: #ffffff;
        width: 80%;

        > div {
          position: relative;
          left: 50%;
          transform: translateX(-50%);
        }

        .head {
          margin-top: -8%;
        }

        .name {
          text-align: center;
          font-weight: 600;
          font-size: 30px;

          * {
            display: inline;
          }
        }

        .other{
          font-size: 25px;
          display: flex;
          justify-content: center;
          margin-top: 10px;

          *{
            margin: 0 10px;
          }
        }

        .handle{
          margin-top: 20px;
          margin-bottom: 10px;
          display: flex;
          width: 90%;
          height: 80px;

          .but {
            width: 240px;
            height: 80px;
          }

          .cancel {
            background-color: #99c2ff;
          }

          > * {
            margin: 0 20px;
          }
        }
      }

      .relative {

      }
    }
  }

</style>
