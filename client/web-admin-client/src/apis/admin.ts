import request from 'utils/request';

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


