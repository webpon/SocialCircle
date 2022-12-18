<template>
  <div :class="childClass">
    <div>
      <UserHead :userId="comment.userId" :twoUserId="userId" :headSize="30" :fontWeight="500"
                @updateName="updateName"/>
      <div class="content" @click="repl">
        <p @touchstart.prevent="gotouchstart" @touchend.prevent="gotouchend">{{comment.content}}</p>
        <div class="like" @click="onLike">
          <van-icon name="like-o" size="18" v-if="!like"/>
          <van-icon name="like" size="18" v-else color="#ee0a24"/>
          <div>{{comment.likeNum}}</div>
        </div>
      </div>
      <div class="time">
        {{timeStr}}
      </div>
    </div>
    <nav v-show="commentShow">
      <template v-for="item in comment.childList">
        <Comment :comment="item" :child="comment.parentId === 0" :userId="comment.userId"/>
      </template>
    </nav>
    <div v-if="comment.parentId === 0 && comment.childList.length > 0">
      <span v-if="!commentShow" @click="commentShow= true">
        展示回复
        <van-icon name="arrow-down"/>
      </span>
      <span v-else @click="commentShow = false">
        收起
        <van-icon name="arrow-up"/>
      </span>
    </div>
    <van-action-sheet :show="actionShow" :actions="actions" @select="onSelect" />
  </div>
</template>

<script setup lang="ts">
  import CommentType from "@/type/Comment.type";
  import UserHead from "@/views/home/components/UserHead.vue"
  import {getCurrentInstance, ref, Ref} from "vue";
  import {likeByComment, whetherLikeByComment} from "@/api/like";
  import {deleteComment as deleteCommentApi} from "@/api/comment";
  import {useUserStore} from "@/store/modules/user";

  interface por {
    comment: CommentType;
    child: boolean;
    userId: number;
    reply: Function
  }

  const {comment, child, userId} = withDefaults(defineProps<por>(), {
    userId: 0,
    reply: () => {
    }
  })
  const childClass = ref(!child ? '' : 'child');
  const commentShow = ref(false);
  const timeStr = ref("");

  let date = new Date(comment.createTime as number);  // 参数需要毫秒数，所以这里将秒数乘于 1000
  const Y = date.getFullYear() + '-';
  const M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
  const D = date.getDate() + ' ';
  const now = new Date();
  const Y1 = now.getFullYear() + '-';
  const D1 = now.getDate() + ' ';
  if (Y !== Y1) {
    timeStr.value += Y;
  }
  if (D !== D1) {
    timeStr.value += M;
    timeStr.value += D;
  }
  timeStr.value += (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
  const minutes = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes());
  timeStr.value += minutes

  const like = ref(false);
  whetherLikeByComment({commentId: comment.id as number})
    .then(() => like.value = true)
    .catch(() => like.value = false)
  const onLike = () => {
    likeByComment({commentId: comment.id as number}).then(() => {
      if (like.value) {
        (comment.likeNum as number)--
      } else {
        (comment.likeNum as number)++
      }
      like.value = !like.value
    })
  }
  let patName;
  const updateName = (name1) => patName = name1;
  const {proxy} = getCurrentInstance()
  const repl = () => {
    proxy.mittBus.emit('comment', {
      name: `回复 ${patName}`,
      func: (c) => comment.childList?.push(c),
      id: comment.id
    });
  }

  const userStore = useUserStore();
  const { id } = userStore.getUserInfo;
  const actionShow = ref(false);
  const actions = [
    { name: '复制', id: 1}
  ];
  // if (id === comment.userId){
    actions.push({ name: '删除', id: 2})
  // }
  actions.push({ name: '取消', id: 3})
  const onSelect = (item) => {
    switch (item.id) {
      case 1:
        if (navigator && navigator.clipboard) {
          navigator.clipboard.writeText(comment.content as string)
        }
        break;
      case 2:
        deleteCommentApi(comment.id)
        deleteComment()

    }
    actionShow.value = false;
  };

  let  timeOut;
  const gotouchstart = () => {
    clearTimeout()
    timeOut = setTimeout(() => actionShow.value = true, 500)
  }
  const gotouchend = () =>  clearTimeout(timeOut);

  const deleteComment = ()=>{
    proxy.mittBus.emit('deleteComment', comment.id);
  };

</script>

<style scoped lang="less">
  .child {
    margin: 5px 30px;
  }

  div {

    p, span, .time {
      margin: 0 55px;
      word-break: break-all;
    }

    .time {
      color: #999999;
    }

    span {
      color: #2B85E4;
    }

    .content {
      display: flex;
      align-items: center;
      justify-content: space-between;

      .like {
        width: 100px;
        text-align: center;
        float: right;
      }

      p {
        margin-bottom: 10px;
        font-size: 27px;
        width: 90%;
        margin-right: 10px;
      }
    }
  }
</style>
