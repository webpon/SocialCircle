import {http} from "@/utils/http/axios";

const base = "/like"
/**
 * @description: 是否点赞
 */
export function whetherLikeByDynamic(params: { dynamicId: number }) {
  return http.request<API.BasicResponseModel>(
    {
      url: `${base}/dynamic/whether`,
      method: 'get',
      params,
    }
  );
}
/**
 * @description: 点赞/取消 动态
 */
export function likeByDynamic(params: { dynamicId: number }) {
  return http.request<API.BasicResponseModel>(
    {
      url: `${base}/dynamic`,
      method: 'POST',
      params,
    }
  );
}
