import React, { memo } from 'react';
import { BrowserRouterProps } from 'react-router-dom';


const User: React.FC<BrowserRouterProps> = () => {
  return (
    <div>
        用户列表
    </div>
  );
};

export default memo(User);
