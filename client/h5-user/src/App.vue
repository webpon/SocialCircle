<template>
  <vanConfigProvider :theme="getDarkMode" :theme-vars="getThemeVars()">
    <routerView v-slot="{ Component }">
      <transition :name="getTransitionName" mode="out-in" appear>
        <KeepAlive>
          <component :is="Component" />
        </KeepAlive>
      </transition>
    </routerView>
  </vanConfigProvider>
</template>

<script setup lang="ts">
  import { computed, unref } from 'vue';
  import { darken, lighten } from '@/utils/index';
  import { useRouteStore } from '@/store/modules/route';
  import { useDesignSetting } from '@/hooks/setting/useDesignSetting';
  import {useUserStore} from "@/store/modules/user";
  import {CURRENT_USER} from "@/store/mutation-types";
  import {createStorage} from "@/utils/Storage";

  const routeStore = useRouteStore();
  const { getDarkMode, getAppTheme, getIsPageAnimate, getPageAnimateType } = useDesignSetting();

  // 需要缓存的路由组件
  const keepAliveComponents = computed(() => routeStore.keepAliveComponents);

  const getThemeVars = () => {
    const appTheme = unref(getAppTheme);
    const darkenStr = darken(appTheme, 25);
    const lightenStr = lighten(appTheme, 10);

    return {
      actionSheetCancelTextColor: appTheme,
      buttonPrimaryBackground: appTheme,
      buttonPrimaryBorderColor: appTheme,
      radioCheckedIconColor: appTheme,
      sliderActiveBackground: appTheme,
      cascaderActiveColor: appTheme,
      checkboxCheckedIconColor: appTheme,
      numberKeyboardButtonBackground: appTheme,
      pickerLoadingIconColor: appTheme,
      calendarRangeEdgeBackground: appTheme,
      calendarRangeMiddleColor: appTheme,
      calendarSelectedDayBackground: appTheme,
      stepperButtonRoundThemeColor: appTheme,
      switchOnBackground: appTheme,
      dialogConfirmButtonTextColor: appTheme,
      dropdownMenuOptionActiveColor: appTheme,
      dropdownMenuTitleActiveTextColor: appTheme,
      notifyPrimaryBackground: appTheme,
      circleColor: appTheme,
      noticeBarBackground: lightenStr,
      noticeBarTextColor: darkenStr,
      progressColor: appTheme,
      progressPivotBackground: appTheme,
      stepActiveColor: appTheme,
      stepFinishLineColor: appTheme,
      swipeIndicatorActiveBackground: appTheme,
      tagPrimaryColor: appTheme,
      navBarIconColor: appTheme,
      navBarTextColor: appTheme,
      paginationItemDefaultColor: appTheme,
      sidebarSelectedBorderColor: appTheme,
      tabsDefaultColor: appTheme,
      tabsBottomBarColor: appTheme,
      tabbarItemActiveColor: appTheme,
      treeSelectItemActiveColor: appTheme,
    };
  };

  const Storage = createStorage({ storage: localStorage });
  const userStore = useUserStore();
  Storage.get(CURRENT_USER, '').petName ||userStore.GetUserInfo()

  const getTransitionName = computed(() => {
    return unref(getIsPageAnimate) ? unref(getPageAnimateType) : undefined;
  });
</script>

<style lang="less">
  @import './styles/index.less';
</style>
