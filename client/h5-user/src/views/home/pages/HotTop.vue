<template>
  <div class="overflow-y-auto">
    <div v-for="(item, i) in dynamics">
      <Dynamic :dynamic="item.dynamic" :images="item.images"
               @deleteDyn="deleteDyn" :top="i+1"/>
    </div>
  </div>
</template>

<script setup lang="ts">
  import {ref, watchEffect} from "vue";
  import DynamicVM from "@/type/DynamicVM";
  import {getDynamicByConcern, getDynamicByTop} from "@/api/dynamic";
  import Dynamic from "@/components/Dynamic.vue";
  import useDyanmic from "@/views/home/pages/useDyanmic";

  const dynamics = ref<Array<DynamicVM>>([])
  const p = ref(1);
  let gotoGet = ref(true);


  function getDy() {
    getDynamicByTop().then((data)=>{
      dynamics.value.push(...data)
    })
  }
  getDy();

  const {handleScroll, deleteDyn} = useDyanmic(dynamics, null, getDy,gotoGet);

</script>

<style scoped lang="less">

  .overflow-y-auto {
    overflow: auto;
    margin-bottom: 30px;
  }
</style>
