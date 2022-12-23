<template>
  <div class="bg">
    <div>
      <van-tabs v-model:active="active" swipeable :line-width="20" shrink sticky class="relative">
        <van-tab :title-style="{ fontSize: '17px', margin: '0 5px' }">
          <template #title>
            <van-badge :dot="showMsg">
              关注
            </van-badge>
          </template>
          <Concern />
        </van-tab>
        <van-tab title="推荐" :title-style="{ fontSize: '17px', margin: '0 5px' }">
          <Recommended />
        </van-tab>
        <van-tab title="热榜" :title-style="{ fontSize: '17px', margin: '0 5px' }">
          <HotTop/>
        </van-tab>
      </van-tabs>
      <van-button icon="plus" type="primary" class="!fixed z-100 top-10px right-25px w-65px !h-65px !rounded-full" @click="router.push('/home/postNews');"/>
    </div>
  </div>
</template>

<script setup lang="ts" >
import { computed, ref } from 'vue';
import { useDesignSettingStore } from '@/store/modules/designSetting';
import SvgIcon from '@/components/SvgIcon.vue';
import { useGlobSetting } from '@/hooks/setting';
import Recommended from "./pages/Recommended.vue";
import Concern from "./pages/Concern.vue";
import ClassifyType from "@/type/Classify.type";
import { getClassify } from "@/api/classify";
import Classify from "./pages/Classify.vue";
import HotTop from "./pages/HotTop.vue";
import { useMessageStore } from "@/store/modules/message";
import { useRouter } from 'vue-router';


const active = ref(1);
const designStore = useDesignSettingStore();
const messageStore = useMessageStore();
const globSetting = useGlobSetting();
const router = useRouter();
const { title } = globSetting;

const index = ref(2);
const showMsg = ref(messageStore.getConcernMsgShow);
// const classifies = ref<Array<ClassifyType>>([]);
//
// getClassify().then((data) => {
//   classifies.value.push(...data);
// })
</script>

<style scoped lang="less">
.bg {
  background-color: #eee;

}
</style>
