<template>
  <div class="topic">

    <div class="left">
      <van-sticky>
        <van-pull-refresh v-model="loadingUp" @refresh="onRefresh">
        <van-button type="primary" block>创建</van-button>
          <van-sidebar v-model="active" @change="onChange">
            <van-list
              v-model:loading="loading"
              :finished="finished"
              finished-text="没有更多了"
              @load="onLoad"
            >
              <template v-for="item in topics">
                <van-sidebar-item :title="item.title" :badge="topicNum[item.id]" />
              </template>
            </van-list>
          </van-sidebar>
        </van-pull-refresh>
      </van-sticky>
    </div>
    <div class="right">
      <template v-if="index !== -1">
        <Topic :data="topic"/>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
  import {reactive, ref, watchEffect} from 'vue';
  import {getConcern as getConcernApi} from "@/api/topic";
  import {showToast} from "vant";
  import Topic from './components/Topic.vue';
  import {useMessageStore, useMsgStoreWidthOut} from "@/store/modules/message";

  const messageStore = useMessageStore();
  const active = ref(0);
  const topics = ref([]);
  const topic = ref(null);
  const p = ref(1);
  const index = ref(-1);
  const loading = ref(false);
  const finished = ref(false);
  const loadingUp = ref(false);
  const topicNum = reactive({});

  const getConcern = (p)=>{
    loading.value = true;
    getConcernApi(p).then((data)=>{
      topics.value.push(...data);
      finished.value = data.length !== 10;
      loading.value = false;
      if (p === 1) {
        index.value = 0;
      }
    })
  }


  const onLoad = () => {
    p.value++;
    getConcern(p.value);
  };
  const onRefresh = () => {
    showToast('刷新成功');
    loadingUp.value = false;
    p.value = 0;
    topics.value = [];
    onLoad();
  };
  getConcern(1);
  watchEffect(() => {
    const t = topics.value[index.value];
    // console.log((topics.value[index.value]).id)
    topic.value = t;
    messageStore.setTopicNum(t, 0);
  });
  watchEffect(() => {
    for (const topicNum1 of messageStore.getTopicNum) {
      topicNum[topicNum1.topicId] = topicNum1.msgNum;
    }
  });

  const onChange = (i) => {
    index.value = i;
    console.log(i)
  };

</script>

<style scoped lang="less">
.topic{
  display: flex;

  .left{
    border-right: #bbb .5px solid;
    height: 100%;
  }

  .right{
    width: 100%;
  }
}
</style>
