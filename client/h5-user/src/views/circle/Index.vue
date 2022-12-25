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
                <template v-if="topicNum[item.id]">
                  <van-sidebar-item :title="item.title" :badge="topicNum[item.id]" />
                </template>
                <template v-else>
                  <van-sidebar-item :title="item.title" />
                </template>
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
    <van-action-sheet
      v-model:show="show"
      :actions="actions"
      @select="onSelect"
      cancel-text="取消"
      close-on-click-action
    />
  </div>
</template>
<script lang="ts">
export default {
  name: 'circlePage'
}
</script>
<script setup lang="ts">
  import {reactive, ref, watchEffect} from 'vue';
  import {getConcern as getConcernApi, getMyTopic} from "@/api/topic";
  import {showToast} from "vant";
  import Topic from './components/Topic.vue';
  import {useMessageStore, useMsgStoreWidthOut} from "@/store/modules/message";
  import {deleteComment as deleteCommentApi} from "@/api/comment";
  import {useRouter} from "vue-router";

  const messageStore = useMessageStore();
  const active = ref(0);
  const topics = ref([]);
  const topic = ref(null);
  const p = ref(1);
  const index = ref(-1);
  const loading = ref(false);
  const show = ref(false);
  const finished = ref(false);
  const loadingUp = ref(false);
  const topicNum = reactive({});
  const actions = [
    { name: '选项一', id: 1},
    { name: '选项二', id:2},
  ];
  const onSelect = (item) => {
    switch (item.id) {
      case 1:

        break;
      case 2:

    }
  };
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
    getMyTopic(p).then((data)=>{
      topics.value.push(...data);
      finished.value = data.length !== 10;
      loading.value = false;
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
    topic.value = topics.value[index.value];
  });
  watchEffect(() => {
    if (topic.value) {
      messageStore.setTopicNum(topic.value.id, 0);
    }
  });
  watchEffect(() => {
    let num = messageStore.getTopicNum;
    for (let i = 0; i < num.length; i++) {
      topicNum[num[i].topicId] = num[i].msgNum;
    }
  });

  const onChange = (i) => {
    index.value = i;
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
