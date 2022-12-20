<template>
  <div>
    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
          v-model="formData.content"
          rows="6"
          type="textarea"
          maxlength="200"
          placeholder="记录这一刻的想法"
          label-align="top"
          show-word-limit
          @focus="foc"
        />
      </van-cell-group>
      <van-icon name="smile-o" size="30" @click="showEmo"/>
      <div class="images">
        <van-uploader v-model="files" multiple  :max-count="9" preview-size="100" />
      </div>
      <div style="margin: 16px;">
        <van-button round block type="primary" native-type="submit">
          提交
        </van-button>
      </div>
    </van-form>
    <Emoji @emojiClick="emojiClick" v-show="showEmoji"/>

  </div>
</template>

<script setup lang='ts'>
  import {uploaderFile} from '@/api/oss';
  import {reactive, ref, watchEffect} from 'vue'
  import {showFailToast} from "vant";
  import ImageType from "@/type/Image.type";
  import {addDynamic} from "@/api/dynamic";
  import {useRouter} from "vue-router";

  const formData = reactive<{
    content: string;
  }>({
    content: ''
  });

  const files = ref([])
  const router = useRouter();

  const onSubmit = () => {
    const images:Array<ImageType> = [];
    new Promise<Array<ImageType>>(async (resolve)=>{
      const fileList = files.value;
      // 遍历文件
      for (let i = 0; i < fileList.length; i++) {
        let file =  fileList[i];
        //上传文件
        let url = await uploaderFile(file.file)
        let image: ImageType = {
          sort: i + 1,
          url,
          id: null
        }
        images.push(image);
      }
      resolve(images)
    }).then((images)=>{
      addDynamic({
        images,
        dynamic:{
          content: formData.content
        }
      }).then(({code, msg})=>{
        console.log(code)
        if (code === 200) {
          router.go(-1)
        }else {
          showFailToast(msg || '发布失败');
        }
      })
    })
  }

  const showEmoji = ref(false);
  const emojiClick = (text) => {
    formData.content += text
  }

  const showEmo = () => showEmoji.value = !showEmoji.value;
  const foc = () => showEmoji.value = false;

</script>

<style>
  .images{
    padding: 20px 60px;
    margin: auto;
    width: 820px;
  }
</style>
