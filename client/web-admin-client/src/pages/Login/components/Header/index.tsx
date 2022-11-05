import React from 'react';
import { Button, Tag } from 'tdesign-react';
import { LogoGithubIcon, HelpCircleIcon, SettingIcon } from 'tdesign-icons-react';
import { useAppDispatch } from 'store';
import { toggleSetting } from 'store/global';

import LogoFullIcon from 'assets/svg/assets-logo-full.svg?component';
import Style from './index.module.less';

export default function Header() {
  const dispatch = useAppDispatch();

  const navToGitHub = () => {
    window.open('https://gitee.com/gitopenchina/social-circle/tree/dev/');
  };

  const navToHelper = () => {
    window.open('http://tdesign.tencent.com/starter/docs/react/get-started');
  };

  const toggleSettingPanel = () => {
    dispatch(toggleSetting());
  };
  
  return (
    <div>
      <header className={Style.loginHeader}>
        <Tag shape="round" className={Style.logo}>
          圈子社交
        </Tag>
        <div className={Style.operationsContainer}>
          <Button
            className={Style.operationsButton}
            theme='default'
            shape='square'
            variant='text'
            onClick={navToGitHub}
          >
            <LogoGithubIcon className={Style.icon} />
          </Button>
          <Button
            className={Style.operationsButton}
            theme='default'
            shape='square'
            variant='text'
            onClick={navToHelper}
          >
            <HelpCircleIcon className={Style.icon} />
          </Button>
          <Button
            className={Style.operationsButton}
            theme='default'
            shape='square'
            variant='text'
            onClick={toggleSettingPanel}
          >
            <SettingIcon className={Style.icon} />
          </Button>
        </div>
      </header>
    </div>
  );
}
