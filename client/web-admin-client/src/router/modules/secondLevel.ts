import { lazy } from 'react';
import { ClearIcon } from 'tdesign-icons-react';
import { IRouter } from '../index';

const dashboard: IRouter[] = [
  {
    path: '/trends',
    meta: {
      title: '动态管理',
      Icon: ClearIcon,
    },
    children: [
      {
        path: 'manage',
        Component: lazy(() => import('pages/Dynamic')),
        meta: {
          title: '动态列表',
        },
      },
    ],
  },
];

export default dashboard;
