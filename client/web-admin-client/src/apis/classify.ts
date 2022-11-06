// @ts-ignore
import request from "utils/request";

export const getClassifyApi = async () => {
  return request.get<any, any>(`/classify`);
};

export const deleteClassifyApi = async (id: number) => {
  return request.delete<any, any>(`/classify`, {
    params:{"ids": id}
  });
};

export const addClassifyApi = async (title:string) => {
  return request.post<any, any>(`/classify`, {title});
};
