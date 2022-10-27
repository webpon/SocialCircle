import React, { memo, useEffect } from 'react';
import { BrowserRouterProps } from 'react-router-dom';
import { getUserList } from 'modules/user';
import { useAppDispatch } from 'modules/store';


const User: React.FC<BrowserRouterProps> = () => {
    const dispatch = useAppDispatch();
    useEffect(() => {
        dispatch(
            getUserList(),
        );
        return () => {
            // console.log('clear');
            // dispatch(clearPageState());
        };
    }, []);

    return (
        <div>
            用户列表
        </div>
    );
};

export default memo(User);
