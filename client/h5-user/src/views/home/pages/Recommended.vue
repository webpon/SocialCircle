<template>
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

</template>

<script setup lang="ts">
  import {ref} from "vue";
  import DynamicVM from "@/type/DynamicVM";
  import {getDynamicByRecommended} from "@/api/dynamic";
  import Dynamic from "@/components/Dynamic.vue";
  import useDyanmic from "@/views/home/pages/useDyanmic";
  import {showToast} from "vant";
  const loading = ref(false);
  const finished = ref(false);
  const dynamics = ref<Array<DynamicVM>>([])

  const p = ref(1);

  const loadingUp = ref(false);

  function getDy(p: number) {
    loading.value = true;
    getDynamicByRecommended(p).then((data) => {
      dynamics.value.push(...data)
      finished.value = data.length !== 10;
      loading.value = false;
    })
  }
  console.log('_____________________');
  const {deleteDyn,onLoad,onRefresh} = useDyanmic(dynamics, p, getDy,loadingUp);
  


</script>

<style scoped lang="less">

  .overflow-y-auto {
    overflow: auto;
    margin-bottom: 30px;
  }
</style>
