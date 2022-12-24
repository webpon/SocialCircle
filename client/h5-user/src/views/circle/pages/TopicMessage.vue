<template>
  <div class="topic-message">
    <nav>

    </nav>
    <MessageInput/>
  </div>
</template>

<script setup lang="ts">
  import MessageInput from "@/components/MessageInput.vue";

</script>

<style scoped lang="less">
.topic-message {
  height: 104vw;
  width: 100%;

  nav{
    /*height: 90vw;*/
  }
}</style>
