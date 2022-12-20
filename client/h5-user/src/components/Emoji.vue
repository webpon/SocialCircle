<template>
  <div class="emoji" :style="{width,height}">
    <span v-for="k,v in emoji" :key="k[0]" @click="()=>emit('emojiClick',v)">{{v}}</span>
  </div>
</template>

<script setup lang="ts">
  import {getEmoji} from "@/api/emoji";
  import {getCurrentInstance, onMounted, ref} from "vue";

  interface TProps {
    width:string;
    height:string;
  }

  const {width,height} = withDefaults(
    defineProps<TProps>(), {
      width: "100%",
      height: "250px"
    }
  );
  const emoji = ref()

  onMounted(async () => {
    emoji.value = await getEmoji().catch((d) => d);
  })

  const {emit} = getCurrentInstance()
</script>

<style scoped lang="less">
  .emoji {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    overflow: auto;

    span {
      font-size: 40px;
      margin: 5px;
    }
  }
</style>
