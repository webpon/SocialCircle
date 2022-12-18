import {http} from "@/utils/http/axios";

const base = "/like"
/**
 * @description: 动态是否点赞
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
 * @description: 评论是否点赞
 */
export function whetherLikeByComment(params: { commentId: number }) {
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
/**
 * @description: 点赞/取消 评论
 */
export function likeByComment(params: { commentId: number }) {
  return http.request<API.BasicResponseModel>(
    {
      url: `${base}/comment`,
      method: 'POST',
      params,
    }
  );
}
