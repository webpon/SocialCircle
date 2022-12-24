<template>
  <footer>
    <van-sticky :offset-bottom="0" position="bottom">
      <van-form ref="formRef" @submit="handleSubmit">
        <van-cell-group >
          <van-field
            v-model="msgData.content"
            ref="input"
            rows="1"
            autosize
            type="textarea"
          />
        </van-cell-group>
        <van-icon name="smile-o" size="30" @click="showEmo"/>
        <van-icon name="add-o" size="30" v-if="!msgData.content"/>
        <van-button
          v-else
          type="primary"
          native-type="submit"
        >
          发送
        </van-button>
      </van-form>
    </van-sticky>
    <van-tabs
      v-model:active="active"
      :line-width="20"
      class="relative"
      background="#eee"
      v-show="showEmoji"
    >
      <van-tab :title-style="{ fontSize: '17px', margin: '0 5px' }">
        <template #title>
          <van-icon name="smile-o" size="30"/>
        </template>
        <Emoji @emojiClick="emojiClick"/>
      </van-tab>
      <van-tab title="GIF" :title-style="{ fontSize: '17px', margin: '0 5px' }">
        <GIF/>
      </van-tab>
    </van-tabs>

  </footer>

</template>

<script setup lang="ts">
  import Emoji from "@/components/Emoji.vue";
  import {comment} from "@/api/comment";
  import {reactive, ref} from "vue";
  import {FormInstance} from "vant";
  import GIF from '@/components/GIF.vue'

  /**
   * todo 接收信息类型，和id。直接和ws通讯
   */

  const msgData = reactive({
    content: "",
  })

  const active = ref(0);
  const formRef = ref<FormInstance>();
  function handleSubmit() {
    formRef.value
      ?.validate()
      .then(async () => {

      })
  }

  const showEmoji = ref(false);
  const emojiClick = (text) => {
    msgData.content += text
  }

  const showEmo = () => {
    showEmoji.value = !showEmoji.value
  };
  const foc = () => showEmoji.value = false;


</script>

<style scoped lang="less">
  footer {
    padding: 9px;
    background: #EEEEEE;
    position: fixed;
    width: 78vw;
    max-height: 50vw;
    bottom: 0;

    form {
      align-items: center;
      display: flex;
      justify-content: space-between;
    }

    input {
      height: 10px;
      padding: 0;
    }
  }

</style>
