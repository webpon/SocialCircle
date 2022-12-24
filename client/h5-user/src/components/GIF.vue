<template>
  <div class="gif">
    <van-list
      v-model:loading="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="onLoad"
    >
      <van-image
        width="70"
        height="70"
        v-for="item in gif"
        @click="()=>send(item.url)"
        :src="item.url"
      />
    </van-list>
  </div>

</template>

<script setup lang="ts">

  import {getGIF} from "@/api/emoji";
  import {getCurrentInstance, ref, watchEffect} from "vue";

  const loading = ref(false);
  const finished = ref(false);
  const p = ref(0);
  const gif = ref([]);
  const DATA = ref();
  getGIF().catch(({data})=>{
    DATA.value = data;
  })
  const onLoad = () => {
    p.value++
  };
  watchEffect(() => {
    if (DATA.value) {
      gif.value.push(...DATA.value.slice(p.value, 100));
    }
  });
  const {proxy} = getCurrentInstance();

  const send = (url)=>{
    proxy.$emit("gif", url)
  }

</script>

<style scoped lang="less">
.gif{
  height: 100vw;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  overflow: auto;

}
</style>
