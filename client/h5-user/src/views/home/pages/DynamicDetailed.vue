<template>
  <div>
    <div>
      <UserHead v-if="data.dynamic.userId" :userId="data.dynamic.userId"/>
      <p>
        {{data.dynamic.content}}
      </p>
      <nav>
        <img v-for="item,i in data.images"
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
          分享 ({{data.dynamic.shareNum}})
        </span>
        </div>
        <div @click="onLike">
          <van-icon name="like-o" size="18" v-if="!like"/>
          <van-icon name="like" size="18" v-else color="#ee0a24"/>
          <span>
          点赞 ({{data.dynamic.likeNum}})
        </span>
        </div>
        <van-share-sheet
          :show="showShare"
          title="立即分享给好友"
          :options="options"
          @select="onSelect"
        />
      </div>
    </div>
    <van-divider :style="{ color: '#898989', borderColor: '#898989' }"/>
    <div  @scroll="handleScroll" class="comment">
      <template v-for="item in comments">
        <Comment :comment="item" @updatePlaceholder="updatePlaceholder"/>
      </template>
      <div class="t" v-if="gotoGet">加载中...</div>
      <div class="t" v-else>到底啦</div>
    </div>
    <div class="commentInput">
      <van-form ref="formRef" @submit="handleSubmit">
        <van-field v-model="commentData.content" center clearable class="input"
                   :placeholder="placeholder" ref="input"/>
        <van-button
          type="primary"
          native-type="submit"
        >
          评论
        </van-button>
      </van-form>
    </div>

  </div>
</template>

<script setup lang="ts">
  import UserHead from "@/views/home/components/UserHead.vue"
  import {
    getCurrentInstance,
    onBeforeMount,
    onMounted,
    onUnmounted,
    reactive,
    Ref,
    ref,
    watchEffect
  } from "vue";
  import {likeByDynamic, whetherLikeByDynamic} from "@/api/like";
  import {useRoute} from "vue-router";
  import {getDynamicById} from "@/api/dynamic";
  import DynamicVM from "@/type/DynamicVM";
  import {FormInstance} from "vant";
  import CommentType from "@/type/Comment.type";
  import {comment, getComment} from "@/api/comment";
  import Comment from "@/views/home/components/Comment.vue";
  import cellGroup
    from "../../../../../mini-program-user-client/uni_modules/uview-ui/libs/config/props/cellGroup";

  const route = useRoute()

  let data = reactive<DynamicVM>({
    images: [],
    dynamic: {}
  })
  const like = ref(false);
  const {id} = route.params
  const gotoGet = ref(true);
  const input = ref() as Ref;

  const images1 = [];
  const width = ref(110)
  onBeforeMount(async () => {
    const d = await getDynamicById(id);
    data.images = d?.images
    data.dynamic = d?.dynamic

    whetherLikeByDynamic({dynamicId: data.dynamic.id})
      .then(() => like.value = true)
      .catch(() => like.value = false)

    data.images?.forEach((i) => images1.push(i.url))
    data.images?.sort((a, b) => {
      return a.sort - b.sort
    })

    switch (data.images?.length) {
      case 1:
        width.value = 350;
        break
      case 2:
        width.value = 167;
        break
    }
  })

  //' 展示图片
  const show = ref(false);
  const index = ref(0);
  const onChange = (newIndex) => {
    index.value = newIndex;
    show.value = true;
  };

  // 分享
  const showShare = ref(false);
  const options = [
    {name: '微信', icon: 'wechat', id: 1},
    {name: '复制链接', icon: 'link', id: 2},
  ];

  const onSelect = (option) => {
    showShare.value = false;
  };

  const onLike = () => {
    likeByDynamic({dynamicId: data.dynamic.id}).then(() => {
      if (like.value) {
        data.dynamic.likeNum--
      } else {
        data.dynamic.likeNum++
      }
      like.value = !like.value
    })
  }

  const formRef = ref<FormInstance>();
  const commentData = reactive({
    content: "",
    parentId: 0
  })

  let commentFun = null;

  function handleSubmit() {
    formRef.value
      ?.validate()
      .then(async () => {
        return await comment({
          dynamicId: data.dynamic.id,
          content: commentData.content,
          parentId: commentData.parentId
        })
      }).then((data2) => {
      commentData.parentId = 0;
      commentData.content = '';
      placeholder.value = '说说你的看法吧！'
      console.log(commentFun)
      if (commentFun != null) {
        commentFun(data2);
        commentFun = null
        return;
      }
      console.log(data2)
      comments.value.unshift(data2);
    })
  }

  const p = ref(1);
  const comments = ref<Array<CommentType>>([]);
  watchEffect(()=>{
    getComment(id, p.value).then((data) => {
      comments.value.push(...data);
      if (!data){
        gotoGet.value = false;
      }
    })
  });
  const handleScroll= (e) => {
    const {scrollTop, clientHeight, scrollHeight} = e.target
    if (scrollTop + clientHeight >= scrollHeight - 100 && gotoGet.value){
      gotoGet.value = false
      p.value ++
    }
  }

  const placeholder = ref("说说你的看法吧！")
  const updatePlaceholder = ({name, func, id}) => {
    placeholder.value = name;
    commentFun = func;
    commentData.parentId = id;
    input.value.focus();
  }

  const {proxy} = getCurrentInstance()

  onMounted(() => {
    proxy.mittBus.on('deleteComment', deleteComment);
    proxy.mittBus.on('comment', updatePlaceholder);
  })
  onUnmounted(() => {
    proxy.mittBus.off('comment')
    proxy.mittBus.off('deleteComment')
  })

  const deleteComment = (id)=>{
    const data = filter(id,  comments.value);
    comments.value = data;
  };

  const filter = (id, data)=>{
    return data.filter((item)=>{
      if (item.id === id){
        return false;
      }
      item.childList = filter(id, item.childList);
      return true;
    })
  }

</script>

<style scoped lang="less">
  *{
    font-size: 27px;
  }

  div {
    margin: 20px 0;
    border-radius: 10px;
    background-color: #fff;
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

    .comment {
      overflow-y: scroll;
      height: 65%;
      margin-bottom: 170px;

      .t {
        text-align: center;
      }
    }

    .click {
      justify-content: space-between;
      font-size: 17px;

      > div {
        width: 40%;
        text-align: center;
        display: flex;
        justify-content: center;

        span {
          margin: 0 5px;
        }
      }

    }


    .commentInput {
      padding-top: 5px;
      position: fixed;
      bottom: 0;
      width: 100%;
      background-color: #fff;

      form {
        display: flex;
        margin: 0 10px;
        border-left: 2px #5d9dfe solid;
        border-top: 2px #5d9dfe solid;
        border-bottom: 2px #5d9dfe solid;

        button {
          width: 160px;
          height: 60px;
        }

        .input {
          height: 60px;
          margin: 0;
        }
      }
    }
  }

</style>
