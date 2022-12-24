<template>
  <div class="t">
    <header :style="{'background-image':`url(${data.cover})`}">
      <div class="cover">
        <van-icon name="weapp-nav" color="#fff" class="nav" size="1rem"/>
        <nav>
          <div>
            <h1>{{data.title}}</h1>
            <p :class="showText?'showText':''" @click="show">{{data.describe}}</p>
          </div>
          <van-button class="but" type="primary"  @click="router.push(`/home/postNews/${data.id}`);">发布动态</van-button>
        </nav>
      </div>
    </header>
    <van-tabs
      v-model:active="active"
      swipeable
      :line-width="20"
      shrink
      sticky
      class="relative"
      background="#eee"
    >
      <van-tab title="动态" :title-style="{ fontSize: '17px', margin: '0 5px' }">
        <van-pull-refresh v-model="loadingUp" @refresh="onRefresh">
          <van-list
            v-model:loading="loading"
            :finished="finished"
            finished-text="没有更多了"
            @load="onLoad"
          >
            <Dynamic v-for="item in dynamics" :dynamic="item.dynamic" :images="item.images"
                     @deleteDyn="deleteDyn" :key="item.dynamic.id"/>
          </van-list>
        </van-pull-refresh>
      </van-tab>
      <van-tab title="交流" :title-style="{ fontSize: '17px', margin: '0 5px' }">
        <TopicMessage/>
      </van-tab>
    </van-tabs>

  </div>
</template>

<script setup lang="ts">
  import {Ref, ref} from "vue";
  import Topic from "@/type/Topic.type";
  import DynamicVM from "@/type/DynamicVM";
  import {showToast} from "vant";
  import {getDynamicByTopicId} from "@/api/dynamic";
  import {useRouter} from "vue-router";
  import TopicMessage from "../pages/TopicMessage.vue";

  const loading = ref(false);
  const finished = ref(false);
  const dynamics = ref<Array<DynamicVM>>([])
  const p = ref(1);
  const showCon = ref(false)
  const showText = ref(true)
  const active = ref(0);
  const loadingUp = ref(false);
  const router = useRouter();

  interface TProps {
    data: Topic
  }

  const {data} = defineProps<TProps>();

  function getDyn(p: number) {
    getDynamicByTopicId(data.id, p).then((d)=>{
      dynamics.value.push(...d);
      finished.value = d.length !== 10;
      loading.value = false;
    })
  }

  const onLoad = () => {
    p.value++;
    getDyn(p.value);
  };
  const onRefresh = () => {
    showToast('刷新成功');
    loadingUp.value = false;
    p.value = 0;
    dynamics.value = [];
    onLoad();
  };

  const deleteDyn = (id) => {
    dynamics.value = dynamics.value.filter((item) => item.dynamic.id !== id);
  }

  const show = ()=>showText.value = !showText.value

</script>

<style scoped lang="less">
  .t{
    height: 100%;
  }
  header {
    width: 100%;
    background-size: 100% 11rem;
    position: relative;

    .cover {
      height: 11rem !important;
      width: 100%;
      z-index: 100;
      background-image: linear-gradient(to bottom, rgba(255,0,0,0), rgba(0,0,0,.6));

      .nav {
        float: right;
        margin-right: 26vw;
        margin-top: 10px;
      }

      nav {
        width: 73%;
        position: absolute;
        bottom: 10px;
        left: 20px;
        color: #ffffff;
        display: flex;
        justify-content: space-between;

        div {
          width: 60%;

          h1 {
            font-weight: 600;
            font-size: 40px;
          }

          p {
            overflow: hidden;
            text-overflow:ellipsis;
            word-wrap: break-word;
            display: -webkit-box;
            -webkit-box-orient: vertical;

            text-indent: 2em;
            word-wrap: break-word;
            word-break: break-all;
          }
          .showText{
            -webkit-line-clamp: 2;
          }
        }
      }
    }

    .but {

    }
  }

</style>
