<template>
  <div class="bg">
    <div>
      <van-tabs v-model:active="active">
        <van-tab>
          <template #title>
            <van-badge :dot="showMsg">
              关注
            </van-badge>
          </template>
          <Concern/>
        </van-tab>
        <van-tab title="推荐">
          <Recommended />
        </van-tab>
        <van-tab v-for="item in classifies" :title="item.title">
            <Classify :id="item.id"/>
        </van-tab>
      </van-tabs>

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
  import {getClassify} from "@/api/classify";
  import Classify from "./pages/Classify.vue";
  import {useMessageStore} from "@/store/modules/message";


  const active = ref(1);
  const designStore = useDesignSettingStore();
  const messageStore = useMessageStore();
  const globSetting = useGlobSetting();
  const { title } = globSetting;

  const index = ref(2);
  const showMsg = ref(messageStore.getConcernMsgShow);
  const classifies = ref<Array<ClassifyType>>([]);

  getClassify().then((data) => {
    // console.log(data)
    classifies.value.push(...data);
  })
</script>

<style scoped lang="less">
  .bg{
    background-color: #eee;

  }
</style>
