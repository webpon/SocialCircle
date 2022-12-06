import { http } from '@/utils/http/axios';


/**
 * @description: 获取图片验证码key
 */
export function getCaptchaKey() {
  return http.request<API.BasicResponseModel>(
    {
      url: '/kaptcha',
      method: 'GET',
    },
  );
}

/**
 * @description: 获取验证码
 */
export function sendEmailCode(params: any) {
  return http.request<API.BasicResponseModel>(
    {
      url: '/code/singIn',
      method: 'GET',
      params
    }
  );
}

/**
 * @description: 用户登录
 */
export function login(params: any) {
  return http.request<API.BasicResponseModel>(
    {
      url: '/login',
      method: 'GET',
      params,
    },
    {
      isTransformResponse: false,
    }
  );
}

/**
 * @description: 用户注册
 */
export function sigIn(params: any) {
  return http.request<API.BasicResponseModel>(
    {
      url: '/signIn',
      method: 'POST',
      params,
    }
  );
}

/**
 * @description: 获取用户信息
 */
export function getUserInfo() {
  return http.request({
    url: '/getUserInfo',
    method: 'get',
  });
}

/**
 * @description: 用户登出
 */
export function doLogout() {
  return http.request({
    url: '/logout',
    method: 'POST',
  });
}

/**
 * @description: 用户修改密码
 */
export function changePassword(params: any, uid: any) {
  return http.request(
    {
      url: `/user/u${uid}/changepw`,
      method: 'POST',
      params,
    },
    {
      isTransformResponse: false,
    }
  );
}
