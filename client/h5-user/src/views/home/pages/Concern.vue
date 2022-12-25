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
  import {ref, watchEffect} from "vue";
  import DynamicVM from "@/type/DynamicVM";
  import {getDynamicByConcern} from "@/api/dynamic";
  import Dynamic from "@/views/home/components/Dynamic.vue";
  import useDyanmic from "@/views/home/pages/useDyanmic";

  const loading = ref(false);
  const finished = ref(false);
  const dynamics = ref<Array<DynamicVM>>([])
  const p = ref(1);
  const loadingUp = ref(false);

  function getDy(p) {
    loading.value = true;
    getDynamicByConcern(p).then((data)=>{
      dynamics.value.push(...data)
      finished.value = data.length !== 10;
      loading.value = false;
    })
  }
  const {deleteDyn,onLoad,onRefresh} = useDyanmic(dynamics, p, getDy,loadingUp);
</script>

<style scoped lang="less">

  .overflow-y-auto {
    overflow: auto;
    margin-bottom: 30px;
  }
</style>
