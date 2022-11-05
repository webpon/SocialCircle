import React, { memo } from 'react';
import { BrowserRouterProps } from 'react-router-dom';
const User: React.FC<BrowserRouterProps> = () => {
  return (
    <div>
        动态列表
    </div>
  );
};

export default memo(User);
