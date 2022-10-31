import React, { memo, useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Menu, MenuValue } from 'tdesign-react';
import router, { IRouter } from 'router';
import { resolve } from 'utils/path';
import { useAppSelector } from 'modules/store';
import { selectGlobal } from 'modules/global';
import MenuLogo from './MenuLogo';
import Style from './Menu.module.less';
import request from 'utils/request';

import secondLevel from 'router/modules/secondLevel'
import adminLevel from 'router/modules/adminLevel'

const { SubMenu, MenuItem, HeadMenu } = Menu;

interface IMenuProps {
  showLogo?: boolean;
  showOperation?: boolean;
}



/**
 * 顶部菜单
 */
export const HeaderMenu = memo(() => {
  const globalState = useAppSelector(selectGlobal);
  const location = useLocation();
  const [active, setActive] = useState<MenuValue>(location.pathname); // todo
  const [menu, setMenu] = useState(router)
  const navigate = useNavigate();
  useEffect(() => {
    request.get<any, any>('/userInfo').then(res => {
      const { data = {} } = res
      const { permission, id } = data
      if (permission === 1) {
        if (id === 1) {
          setMenu([...menu, ...secondLevel, ...adminLevel])
        } else {
          setMenu([...menu, ...secondLevel])
        }
      }
    })
  }, [])
  const renderMenuItems = (menu: IRouter[], parentPath = '') => {
    return menu.map((item) => {
      const { children, meta, path } = item;

      if (!meta || meta?.hidden === true) {
        // 无meta信息 或 hidden == true，路由不显示为菜单
        return null;
      }

      const { Icon, title, single } = meta;
      const routerPath = resolve(parentPath, path);

      if (!children || children.length === 0) {
        return (
          <MenuItem
            key={routerPath}
            value={routerPath}
            icon={Icon ? <Icon /> : undefined}
            onClick={() => navigate(routerPath)}
          >
            {title}
          </MenuItem>
        );
      }

      if (single && children?.length > 0) {
        const firstChild = children[0];
        if (firstChild?.meta && !firstChild?.meta?.hidden) {
          const { Icon, title } = meta;
          const singlePath = resolve(resolve(parentPath, path), firstChild.path);
          return (
            <MenuItem
              key={singlePath}
              value={singlePath}
              icon={Icon ? <Icon /> : undefined}
              onClick={() => navigate(singlePath)}
            >
              {title}
            </MenuItem>
          );
        }
      }

      return (
        <SubMenu key={routerPath} value={routerPath} title={title} icon={Icon ? <Icon /> : undefined}>
          {renderMenuItems(children, routerPath)}
        </SubMenu>
      );
    });
  };
  return (
    <HeadMenu
      expandType='popup'
      style={{ marginBottom: 20 }}
      value={active}
      theme={globalState.theme}
      onChange={(v) => setActive(v)}
    >
      {renderMenuItems(menu)}
    </HeadMenu>
  );
});

/**
 * 左侧菜单
 */
export default memo((props: IMenuProps) => {
  const [menu, setMenu] = useState(router)
  const navigate = useNavigate();
  useEffect(() => {
    request.get<any, any>('/userInfo').then(res => {
      const { data = {} } = res
      const { permission, id } = data
      if (permission === 1) {
        if (id === 1) {
          setMenu([...menu, ...secondLevel, ...adminLevel])
        } else {
          setMenu([...menu, ...secondLevel])
        }
      }
    })
  }, [])
  const renderMenuItems = (menu: IRouter[], parentPath = '') => {
    return menu.map((item) => {
      const { children, meta, path } = item;

      if (!meta || meta?.hidden === true) {
        // 无meta信息 或 hidden == true，路由不显示为菜单
        return null;
      }

      const { Icon, title, single } = meta;
      const routerPath = resolve(parentPath, path);

      if (!children || children.length === 0) {
        return (
          <MenuItem
            key={routerPath}
            value={routerPath}
            icon={Icon ? <Icon /> : undefined}
            onClick={() => navigate(routerPath)}
          >
            {title}
          </MenuItem>
        );
      }

      if (single && children?.length > 0) {
        const firstChild = children[0];
        if (firstChild?.meta && !firstChild?.meta?.hidden) {
          const { Icon, title } = meta;
          const singlePath = resolve(resolve(parentPath, path), firstChild.path);
          
          
          return (
            <MenuItem
              key={singlePath}
              value={singlePath}
              icon={Icon ? <Icon /> : undefined}
              onClick={() => navigate(singlePath)}
            >
              {title}
            </MenuItem>
          );
        }
      }
      console.log(routerPath, 'routerPath');
      
      return (
        <SubMenu key={routerPath} value={routerPath} title={title} icon={Icon ? <Icon /> : undefined}>
          {renderMenuItems(children, routerPath)}
        </SubMenu>
      );
    });
  };
  console.log(menu, 'menu');

  const location = useLocation();
  const defaultExpanded = ["/" + location.pathname.split("/")[1]]
  const globalState = useAppSelector(selectGlobal);

  const { version } = globalState;
  const bottomText = globalState.collapsed ? version : `圈子社交管理后台 ${version}`;

  return (
    <Menu
      width='232px'
      style={{ flexShrink: 0, height: '100%' }}
      value={location.pathname}
      theme={globalState.theme}
      collapsed={globalState.collapsed}
      defaultExpanded={defaultExpanded}
      operations={props.showOperation ? <div className={Style.menuTip}>{bottomText}</div> : undefined}
      logo={props.showLogo ? <MenuLogo collapsed={globalState.collapsed} /> : undefined}
    >
      {renderMenuItems(menu)}
    </Menu>
  );
});
