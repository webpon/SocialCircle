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
 * @description: 获取指定用户动态
 */
export function getDynamicByUserId(userId, p: number=1) {
  return http.request<API.BasicResponseModel<Array<DynamicVM>>>(
    {
      url: `${baseUri}/byUserId`,
      method: 'GET',
      params:{p,userId}
    },
  );
}

/**
 * @description: 获取top动态
 */
export function getDynamicByTop() {
  return http.request<API.BasicResponseModel<Array<DynamicVM>>>(
    {
      url: `${baseUri}/top`,
      method: 'GET',
    },
  );
}

/**
 * @description: 按照topicId获取动态
 */
export function getDynamicByTopicId(topicId:number,p:number) {
  return http.request<API.BasicResponseModel<Array<DynamicVM>>>(
    {
      url: `${baseUri}/byTopicId`,
      method: 'GET',
      params:{topicId,p}
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
