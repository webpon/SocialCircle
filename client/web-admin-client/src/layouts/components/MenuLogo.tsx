import React, { memo } from 'react';
import Style from './Menu.module.less'
import { useNavigate } from 'react-router-dom';

interface IProps {
  collapsed?: boolean;
}

export default memo((props: IProps) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate('/');
  };

  return (
    <div  onClick={handleClick}>
      {props.collapsed ? <div className={Style.menuMiniLogo}>圈子</div> : <div className={Style.menuLogo}>圈子社交</div>}
    </div>
  );
});
