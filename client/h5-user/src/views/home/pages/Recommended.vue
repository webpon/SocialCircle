<template>
  <div @scroll="handleScroll" class="overflow-y-auto">
    <Dynamic v-for="item in dynamics" :dynamic="item.dynamic" :images="item.images"
             @deleteDyn="deleteDyn"/>
  </div>
</template>

<script setup lang="ts">
  import {ref, watchEffect} from "vue";
  import DynamicVM from "@/type/DynamicVM";
  import {getDynamicByRecommended} from "@/api/dynamic";
  import Dynamic from "@/components/Dynamic.vue";
  import useDyanmic from "@/views/home/pages/useDyanmic";

  const dynamics = ref<Array<DynamicVM>>([])
  const p = ref(1);
  let gotoGet = ref(true);

  function getDy(p: number) {
    getDynamicByRecommended(p).then((data) => {
      dynamics.value.push(...data)
      gotoGet.value = data.length === 10
    })
  }

  const {handleScroll, deleteDyn} = useDyanmic(dynamics, p, getDy,gotoGet);


</script>

<style scoped lang="less">

  .overflow-y-auto {
    overflow: auto;
    margin-bottom: 30px;
  }
</style>
