import request from "../utils/request";

export const getHobbyApi = async () => {
  const {data} = await request.get<any, any>(`/hobby`);
  return data;
};

export const deleteHobbyApi = async (id: number) => {
  const {msg, code} = await request.delete<any, any>(`/hobby`, {params: {ids: id}});
  return {msg, code};
};
export const addHobbyApi = async (data: { "title": string, "hobbyName": string }) => {
  const {msg, code} = await request.post<any, any>(`/hobby`, data);
  return {msg, code};
};
