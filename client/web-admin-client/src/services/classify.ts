// @ts-ignore
import request from "utils/request";

// !TOdO: TS类型限制
export const getClassifyApi = async () => {
  return request.get<any, any>(`/classify`);
};

// !TOdO: TS类型限制
export const deleteClassifyApi = async (id: number) => {
  return request.delete<any, any>(`/classify`, {
    params:{"ids": id}
  });
};

// !TOdO: TS类型限制
export const addClassifyApi = async (title:string) => {
  return request.post<any, any>(`/classify`, {title});
};
