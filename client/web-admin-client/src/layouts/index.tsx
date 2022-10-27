import React, { memo, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Drawer, Layout } from 'tdesign-react';
import throttle from 'lodash/throttle';
import { useAppSelector, useAppDispatch } from 'modules/store';
import { selectGlobal, toggleSetting, toggleMenu, ELayout, switchTheme } from 'modules/global';
import Setting from './components/Setting';
import AppLayout from './components/AppLayout';
import Style from './index.module.less';

export default memo(() => {
  const globalState = useAppSelector(selectGlobal);
  const dispatch = useAppDispatch();
  const AppContainer = AppLayout[globalState.isFullPage ? ELayout.fullPage : globalState.layout];

  const useEffectFun = async () => {
    dispatch(switchTheme(globalState.theme));
    const handleResize = throttle(() => {
      if (window.innerWidth < 900) {
        dispatch(toggleMenu(true));
      } else if (window.innerWidth > 1000) {
        dispatch(toggleMenu(false));
      }
    }, 100);
    window.addEventListener('resize', handleResize);
    // 获取用户信息
    // if (Token) {
    //   console.log('_____________');
      
    //   dispatch(getUserInfo())
    // } else {
    //   await dispatch(logout());
    //   navigate('/login');
    // }
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }

  useEffect(() => {
    useEffectFun()
  }, []);

  return (
    <Layout className={Style.panel}>
      <AppContainer />
      <Drawer
        destroyOnClose
        visible={globalState.setting}
        size='458px'
        footer={false}
        header='页面配置'
        onClose={() => dispatch(toggleSetting())}
      >
        <Setting />
      </Drawer>
    </Layout>
  );
});
