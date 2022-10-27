import request from 'utils/request';
export interface IData {
    code: number;
    msg: string;
    data: string;
}


interface IParams {
    email?: string;
    accountId?: string;
    password: string;
}

export const login = async (params: IParams) => {
    return request.get<any, IData>('/login', {
        params
    });
};

// !TOdO: TS类型限制
export const logout = async () => {
    return request.get<any, any>('/logout');
};

// !TOdO: TS类型限制
export const getUserInfo = async () => {
    return request.get<any, any>('/userInfo');
};

// !TOdO: TS类型限制
export const getUserList = async (params: any) => {
    return request.get<any, any>('/manage/user', params);
};

