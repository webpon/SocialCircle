import {http} from "@/utils/http/axios";

/**
 * @description: 获取服务器时间
 */
export function getServerTime() {
  return http.request<API.BasicResponseModel<number>>(
    {
      url: `/time`,
      method: 'get'
    }
  );
}
