<template>
  <div @scroll="handleScroll" class="overflow-y-auto">
    <Dynamic v-for="item in dynamics" :dynamic="item.dynamic" :images="item.images"/>
  </div>
</template>

<script setup lang="ts">
  import {ref, watchEffect} from "vue";
  import DynamicVM from "@/type/DynamicVM";
  import { getDynamicByRecommended} from "@/api/dynamic";
  import Dynamic from "@/components/Dynamic.vue";

  const dynamics = ref<Array<DynamicVM>>([])
  const p = ref(1);
  let  gotoGet = true;

  function getDy(p : number) {
    getDynamicByRecommended(p).then((data)=>{
      dynamics.value.push(...data)
      gotoGet = data.length === 10
    })
  }

  watchEffect(()=>{
     getDy(p.value)
  });
  const handleScroll= (e) => {
    const {scrollTop, clientHeight, scrollHeight} = e.target
    if (scrollTop + clientHeight >= scrollHeight - 100 && gotoGet){
      gotoGet = false
      p.value ++
    }
  }

</script>

<style scoped lang="less">

</style>
