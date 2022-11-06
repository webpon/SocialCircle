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
      {
        path: 'classify',
        Component: lazy(() => import('pages/Dynamic/pages/Classify')),
        meta: {
          title: '分类管理',
        },
      },
      {
        path: 'topic',
        Component: lazy(() => import('pages/Dynamic/pages/Topic')),
        meta: {
          title: '话题管理',
        },
      },
    ],
  },
];

export default dashboard;
