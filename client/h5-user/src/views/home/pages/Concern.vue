<template>
  <div>
    关注
  </div>
</template>

<script setup lang="ts">

  import DynamicVM from "@/type/DynamicVM";
  import { ref, onBeforeUpdate } from "vue";
  import {getDynamicByConcern} from "@/api/dynamic";

  const dynamics = ref<Array<DynamicVM>>([])
  const p = ref(1);

  function getDy() {
    getDynamicByConcern(p.value).then((data)=>{
        dynamics.value.push(...data)
    })
  }

  getDy()
</script>

<style scoped lang="less">

</style>
