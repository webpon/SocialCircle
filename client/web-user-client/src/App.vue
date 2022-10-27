<script setup lang="ts">
</script>

<template>
  <div v-if="true">
    <!-- <transition name="transitionLeft"> -->
    <keep-alive>
      <router-view class="transitionBody"></router-view>
    </keep-alive>
    <!-- </transition> -->
  </div>
  <div v-else>
    <keep-alive>
      <router-view />
    </keep-alive>
  </div>
</template>

<style scoped lang="scss">
.transitionBody {
  transition: all 0.3s ease-out; //设置动画
}

.transitionLeft-enter,
.transitionRight-leave-to {
  transform: translate(100%, 0);
}

.transitionLeft-enter-to {
  transform: translate(0, 0);
}

.transitionLeft-leave-to,
.transitionRight-enter {
  transform: translate(-100%, 0);
}

.transitionLeft-enter-active,
.transitionRight-enter-active {
  //防止过渡时元素抖动
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
  bottom: 0;
}
</style>
