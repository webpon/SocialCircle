import request from "../utils/request";

export const getTopicApi = async (params :{p:number}) => {
  let {data, total} = await request.get<any, any>(`/topic`, {params})
  if (total === 0){
    let {data, total} = await request.get<any, any>(`/topic`, {params})
    return {data, total}
  }
  return {data, total}
};
export const deleteTopicApi = async (id:number) => {
  return request.delete<any, any>(`/topic`, {params:{ids:id}});
};
export const addTopicApi = async (data :{title:string,describe:string}) => {
  return request.post<any, any>(`/topic`, data);
};
