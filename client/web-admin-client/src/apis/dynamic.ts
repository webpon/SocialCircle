import request from "../utils/request";

// @ts-ignore
export const getDynamicApi = async (params: { p: number }) => {
  const {data, total, code} = await request.get<any, any>(`/dynamic`, {params});
  return{data, code, total};
};

export const deleteDynamicApi = async (id: number) => {
  const {data, msg, code} = await request.delete<any, any>(`/dynamic`, {params:{ids:id}});
  return{data, code, msg};
};
