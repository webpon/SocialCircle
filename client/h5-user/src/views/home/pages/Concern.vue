<template>
  <div @scroll="handleScroll" class="overflow-y-auto">
    <Dynamic v-for="item in dynamics" :dynamic="item.dynamic" :images="item.images"
             @deleteDyn="deleteDyn"/>
  </div>
</template>

<script setup lang="ts">
  import {ref, watchEffect} from "vue";
  import DynamicVM from "@/type/DynamicVM";
  import {getDynamicByConcern} from "@/api/dynamic";
  import Dynamic from "@/components/Dynamic.vue";

  const dynamics = ref<Array<DynamicVM>>([])
  const p = ref(1);
  let gotoGet = true;


  function getDy(p) {
    getDynamicByConcern(p).then((data)=>{
      dynamics.value.push(...data)
    })
  }

  const deleteDyn = (id) => {
    dynamics.value = dynamics.value.filter((item) => item.dynamic.id !== id);
  }

  watchEffect(() => {
    getDy(p.value)
  });
  const handleScroll = (e) => {
    const {scrollTop, clientHeight, scrollHeight} = e.target
    if (scrollTop + clientHeight >= scrollHeight - 100 && gotoGet) {
      gotoGet = false
      p.value++
    }
  }

</script>

<style scoped lang="less">

  .overflow-y-auto {
    overflow: auto;
    margin-bottom: 30px;
  }
</style>
