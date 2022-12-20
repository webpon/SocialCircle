<template>
  <div>
    <div class="top">
      <UserHead :userId="dynamic.userId" :fontSiez="30" :time="dynamic.publishTime"/>
      <van-icon name="cross" class="cross" v-if="delShow" @click="deleteDyn"/>
    </div>
    <p @click="pushWithQuery">
      {{dynamic.content}}
    </p>
    <nav>
      <img v-for="item,i in images"
           :style="{
            'height': `${width}px`,
            'width': `${width}px`
           }"
           :src="item.url"
           @click="()=>onChange(i)"
      />
      <van-image-preview v-model:show="show" :images="images1" @change="onChange"
                         @click="()=>show.value = false">
        <template v-slot:index>第{{ index+1 }}页</template>
        <template #image="{ src }">
          <video style="width: 100%;" controls>
            <source :src="src"/>
          </video>
        </template>
      </van-image-preview>
    </nav>
    <div class="flex click">
      <div @click="showShare = true">
        <van-icon name="share-o" size="18"/>
        <span>
          分享 ({{dynamic.shareNum}})
        </span>
      </div>
      <div @click="onLike">
        <van-icon name="like-o" size="18" v-if="!like"/>
        <van-icon name="like" size="18" v-else color="#ee0a24"/>
        <span>
          点赞 ({{dynamic.likeNum}})
        </span>
      </div>
      <div @click="pushWithQuery">
        <van-icon name="comment-o" size="18"/>
        <span>
          评论 ({{dynamic.commentNum}})
        </span>
      </div>
      <van-share-sheet
        v-model:show="showShare"
        title="立即分享给好友"
        :options="options"
        @select="onSelect"
      />
    </div>
  </div>
</template>

<script setup lang="ts">

  import ImageType from "@/type/Image.type";
  import DynamicType from "@/type/Dynamic.type";
  import UserHead from "@/views/home/components/UserHead.vue"
  import {getCurrentInstance, ref} from "vue";
  import {likeByDynamic, whetherLikeByDynamic} from "@/api/like";
  import {useRouter} from "vue-router";
  import {useUserStore} from "@/store/modules/user";
  import {deleteDynamicById} from "@/api/dynamic";
  import {showConfirmDialog} from "vant";

  const router = useRouter()


  interface por {
    images?: Array<ImageType>;
    dynamic: DynamicType;
  }

  const {dynamic, images} = defineProps<por>();

  const images1 = [];
  images?.forEach((i) => images1.push(i.url))
  images?.sort((a, b) => {
    return a.sort - b.sort
  })

  const show = ref(false);
  const index = ref(0);
  const onChange = (newIndex) => {
    index.value = newIndex;
    show.value = true;
  };

  const width = ref(110)
  switch (images?.length) {
    case 1:
      width.value = 350;
      break
    case 2:
      width.value = 167;
      break

  }

  const showShare = ref(false);
  const options = [
    {name: '微信', icon: 'wechat', id: 1},
    {name: '复制链接', icon: 'link', id: 2},
  ];
  const onSelect = (option) => {
    showShare.value = false;
  };

  const like = ref(false);
  whetherLikeByDynamic({dynamicId: dynamic.id}).then(() => like.value = true).catch(() => like.value = false)

  const onLike = () => {
    likeByDynamic({dynamicId: dynamic.id}).then(() => {
      if (like.value) {
        dynamic.likeNum--
      } else {
        dynamic.likeNum++
      }
      like.value = !like.value
    })
  }


  function pushWithQuery() {
    router.push({
      name: 'detailed',
      params: {id: dynamic.id}
    })
  }

  const userStore = useUserStore();
  const {id} = userStore.getUserInfo;
  const delShow = ref(id === dynamic.userId);

  const {proxy} = getCurrentInstance()

  const deleteDyn = () => {
    showConfirmDialog({
      message:
        '是否删除当前动态',
    })
      .then(async () => {
        deleteDynamicById(dynamic.id)
          .then(() => proxy.$emit('deleteDyn', dynamic.id))
          .catch(() => {})
      })
      .catch(() => {});
  }
</script>

<style scoped lang="less">
  * {
    font-size: 27px;
  }

  div {
    margin: 20px 20px;
    padding: 10px;
    border-radius: 10px;
    background-color: #fff;

    .top {
      padding: 0;
      margin: 0;
      display: flex;
      justify-content: space-between;

      .cross {
        position: relative;
        top: 10px;
        right: 10px;
      }
    }

    p, nav {
      margin: 0 20px !important;
    }

    p {
      text-indent: 2em;
      margin: 10px 0;
    }

    nav {
      display: flex;
      justify-content: space-between;
      flex-wrap: wrap;

      img {
        margin: 3px 0;
      }
    }

    .click {
      justify-content: space-between;

      > div {
        width: 40%;
        text-align: center;
        display: flex;
        justify-content: center;

        span {
          margin: 0 5px;
          font-size: 20px !important;
        }
      }

      > div:nth-child(2) {
        border-left: 2px #8888 solid;
        border-right: 2px #8888 solid;
      }
    }
  }
</style>
