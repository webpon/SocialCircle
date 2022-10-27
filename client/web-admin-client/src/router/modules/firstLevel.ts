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
        Component: lazy(() => import('pages/User/List')),
        meta: {
          title: '用户列表',
        },
      },
      {
        path: 'close',
        Component: lazy(() => import('pages/User/Close')),
        meta: {
          title: '违禁管理',
        },
      },
    ],
  },
];

export default dashboard;
