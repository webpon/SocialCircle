import { lazy } from 'react';
import { UserIcon } from 'tdesign-icons-react';
import { IRouter } from '../index';

const dashboard: IRouter[] = [
  {
    path: '/user',
    meta: {
      title: '用户管理',
      Icon: UserIcon,
    },
    children: [
      {
        path: 'manage',
        Component: lazy(() => import('pages/User/List/Index')),
        meta: {
          title: '用户列表',
        },
      },
      {
        path: 'close',
        Component: lazy(() => import('pages/User/Violate/Index')),
        meta: {
          title: '违禁管理',
        },
      },
      {
        path: 'hobby',
        Component: lazy(() => import('pages/User/Hoobby/Index')),
        meta: {
          title: '用户兴趣',
        },
      },
    ],
  },
];

export default dashboard;
