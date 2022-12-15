<template>
  <div>
    {{props.id}}
  </div>
</template>

<script setup lang="ts">

  interface por {
    id:number
  }

  const props = defineProps<por>();

</script>

<style scoped lang="less">

</style>
