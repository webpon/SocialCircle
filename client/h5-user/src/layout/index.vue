<template>
  <div class="h-screen flex flex-col">

    <routerView class="flex-1 overflow-x-hidden">
      <template #default="{ Component, route }">
        <NavBar v-show="!route.meta.hiddenBack" />
        <!--
          keep-alive 标签的 include 属性是根据组件的 name 判断的，
          所以 index.vue 和 list.vue 等页面 vue 文件里一定要写上 name，
          并且与 router 路由表中使用的 name 属性 一致，否则无效
          本项目使用了 vite-plugin-vue-setup-extend 插件
          可直接在 script 标签上书写 name 如：
          <script setup lang="ts" name="DashboardPage">
        -->
        <keep-alive v-if="keepAliveComponents" :include="keepAliveComponents">
          <component :is="Component" :key="route.fullPath" />
        </keep-alive>
        <component v-else :is="Component" :key="route.fullPath" />
      </template>
    </routerView>
    <van-tabbar v-show="route.meta.showTabbar" placeholder route>
      <van-tabbar-item fixed replace v-for="menu in getMenus" :key="menu.name" :to="menu.path"
        :icon="(menu.meta?.icon as string)">{{ menu.meta?.title }}
      </van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useRouteStore } from '@/store/modules/route';
import NavBar from './components/NavBar.vue';

const routeStore = useRouteStore();
// 需要缓存的路由组件
const keepAliveComponents = computed(() => routeStore.keepAliveComponents);

const currentRoute = useRoute();

const getTitle = computed(() => currentRoute.meta.title as string);

// 菜单
const getMenus = computed(() =>
  routeStore.menus.filter((item) => {
    return !item.meta?.innerPage;
  })
);

const getShowHeader = computed(() => !currentRoute.meta.hiddenHeader);

const router = useRouter();
const route = useRoute();


let p = /\/home\/detailed\/\d*/
const dyLeft = ref(p.test(route.path));
watchEffect(() => {
  dyLeft.value = p.test(route.path)
})
const onClickBackDyIndex = () => {
  router.push({ name: "Dashboard" });
}
</script>

<style scoped lang="less">

</style>
