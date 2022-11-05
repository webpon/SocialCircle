import { lazy } from 'react';
import { UserClearIcon } from 'tdesign-icons-react';
import { IRouter } from '../index';

const dashboard: IRouter[] = [
  {
    path: '/admin',
    meta: {
      title: '高级管理',
      Icon: UserClearIcon,
    },
    children: [
      {
        path: 'manage',
        Component: lazy(() => import('pages/Admin/List/Index')),
        meta: {
          title: '管理员列表',
        },
      },
    ],
  },
];

export default dashboard;
