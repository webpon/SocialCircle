import Topic from '@/type/Topic.type';
import { http } from '@/utils/http/axios';

const baseUri = "/topic"


/**
 * @description: 获取圈子
 */
export function getCircle() {
  return http.request<API.BasicResponseModel<Array<Topic>>>(
    {
      url: `${baseUri}`,
      method: 'GET',
    },
  );
}
