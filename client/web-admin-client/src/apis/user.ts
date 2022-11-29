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

export const getUserInfoById = async (params:{id:number}) => {
    const {data}=await request.get<any, any>('/user/info', {params});
    return {data}
};

// !TOdO: TS类型限制
export const getUserList = async (params: any) => {
    return request.get<any, any>('/manage/user', {
        params
    });
};

// !TOdO: TS类型限制
export const getAdminList = async (params: any) => {
    return request.get<any, any>('/admin', {
        params
    });
};

// !TOdO: TS类型限制
export const addAdminUser = async (params: any) => {
    return request.post<any, any>('/admin', params);
};
// !TOdO: TS类型限制
export const updateAdminUser = async (params: any) => {
    return request.put<any, any>('/admin', params);
};

// !TOdO: TS类型限制
export const deleteAdminUser = async (params: any) => {
    return request.delete<any, any>('/admin', {params});
};

// !TOdO: TS类型限制
export const getReportList = async (page: number) => {
    return request.get<any, any>(`/report/${page}`);
};

// !TOdO: TS类型限制
export const deleteReport = async (id: number) => {
    return request.delete<any, any>(`/report/`, {params:{id}});
};

// !TOdO: TS类型限制
export const report = async (data:{userId:number, endTime: string, reason: string}) => {
    return request.post<any, any>(`/sealNumber`, data);
};


