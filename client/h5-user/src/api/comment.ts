import { http } from '@/utils/http/axios';
import ClassifyType from "@/type/Classify.type";
import CommentType from "@/type/Comment.type";

const baseUri = "/comment"


/**
 * @description: 获取评论
 */
export function getComment(dynamicId:number,p:number) {
  return http.request<API.BasicResponseModel<Array<CommentType>>>(
    {
      url: `${baseUri}`,
      method: 'GET',
      params:{dynamicId, p}
    },
  );
}

/**
 * @description: 评论
 */
export function comment(comment:CommentType) {
  return http.request<API.BasicResponseModel<CommentType>>(
    {
      url: `${baseUri}`,
      method: 'POST',
      data: comment
    },
  );
}

/**
 * @description: 删除评论
 */
export function deleteComment(commentId:number) {
  return http.request<API.BasicResponseModel<CommentType>>(
    {
      url: `${baseUri}?commentId=${commentId}`,
      method: 'delete',
    },
  );
}
