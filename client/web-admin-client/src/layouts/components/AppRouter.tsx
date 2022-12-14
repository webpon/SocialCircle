import React, { Suspense, memo } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { Layout, Loading } from 'tdesign-react';
import routers, { IRouter } from 'router';
import secondLevel from 'router/modules/secondLevel'
import adminLevel from 'router/modules/adminLevel'
import { resolve } from 'utils/path';
import Page from './Page';
import Style from './AppRouter.module.less';

const { Content } = Layout;

type TRenderRoutes = (routes: IRouter[], parentPath?: string, breadcrumbs?: string[]) => React.ReactNode[];
/**
 * 渲染应用路由
 * @param routes
 * @param parentPath
 * @param breadcrumb
 */
const renderRoutes: TRenderRoutes = (routes, parentPath = '', breadcrumb = []) =>
  routes.map((route, index: number) => {
    const { Component, children, redirect, meta } = route;
    const currentPath = resolve(parentPath, route.path);
    let currentBreadcrumb = breadcrumb;

    if (meta?.title) {
      currentBreadcrumb = currentBreadcrumb.concat([meta?.title]);
    }

    if (redirect) {
      // 重定向
      return <Route key={index} path={currentPath} element={<Navigate to={redirect} replace />} />;
    }

    if (Component) {
      // 有路由菜单
      return (
        <Route
          key={index}
          path={currentPath}
          element={
            <Page isFullPage={route.isFullPage} breadcrumbs={currentBreadcrumb}>
              {currentPath !== '/login' ? (<RequireAuth><Component /></RequireAuth>) : <Component />}
            </Page>
          }
        />
      );
    }
    // 无路由菜单
    return children ? renderRoutes(children, currentPath, currentBreadcrumb) : null;
  });
const RequireAuth = ({ children }: any) => {
  const isLogin = localStorage.getItem('token') //登录状态逻辑判断
  return isLogin ? (children) : (<Navigate to='/login' replace />);
}
const AppRouter = () => {
  return (
    <Content>
      <Suspense
        fallback={
          <div className={Style.loading}>
            <Loading />
          </div>
        }
      >
        <Routes>{renderRoutes([...routers, ...secondLevel, ...adminLevel])}</Routes>
      </Suspense>
    </Content>
  );
}

export default memo(AppRouter);
