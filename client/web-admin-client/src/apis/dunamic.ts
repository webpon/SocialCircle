import request from "../utils/request";

export const getDynamicApi = async (params: { p: number }) => {
  const {data, total, code} = await request.get<any, any>(`/dynamic`, {params});
  return{data, code, total};
};
