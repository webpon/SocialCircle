<template>
  <div>

  </div>
</template>

<script setup lang="ts">
  import {getCircle} from "@/api/circle";
  import { ref } from "vue";
  import Topic from "@/type/Topic.type";

  const list = ref<Array<Topic>>([]);

  getCircle().then((data)=>{
    list.value.push(...data);
  })
</script>

<style scoped lang="less"></style>
