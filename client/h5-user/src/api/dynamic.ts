import { http } from '@/utils/http/axios';
import DynamicVM from "@/type/DynamicVM";
import DynamicType from "@/type/Dynamic.type";

const baseUri = "/dynamic"


/**
 * @description: 获取关注或者好友的动态
 */
export function getDynamicByConcern(p: number=1) {
  return http.request<API.BasicResponseModel<Array<DynamicVM>>>(
    {
      url: `${baseUri}/concern`,
      method: 'GET',
      params:{p}
    },
  );
}

/**
 * @description: 获取动态
 */
export function getDynamicByRecommended(p: number=1) {
  return http.request<API.BasicResponseModel<Array<DynamicVM>>>(
    {
      url: `${baseUri}`,
      method: 'GET',
      params:{p}
    },
  );
}

/**
 * @description: 按照id获取动态
 */
export function getDynamicById(id: number) {
  return http.request<API.BasicResponseModel<DynamicVM>>(
    {
      url: `${baseUri}/${id}`,
      method: 'GET'
    },
  );
}

/**
 * @description: 按照id删除动态
 */
export function deleteDynamicById(id: number) {
  return http.request<API.BasicResponseModel>(
    {
      url: `${baseUri}?id=${id}`,
      method: 'delete'
    },
    {
      isTransformResponse:true
    }
  );
}

/**
 * @description: 发布动态
 */
export function addDynamic(data: DynamicVM) {
  return http.request<API.BasicResponseModel>(
    {
      url: `${baseUri}`,
      method: 'post',
      data
    },
    {
      isTransformResponse:false
    }
  );
}
