import { http } from '@/utils/http/axios';
import ClassifyType from "@/type/Classify.type";

const baseUri = "/classify"


/**
 * @description: 获取分类
 */
export function getClassify() {
  return http.request<API.BasicResponseModel<Array<ClassifyType>>>(
    {
      url: `${baseUri}`,
      method: 'GET',
    },
  );
}
