import {http} from '@/utils/http/axios';


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
 * @description: 注册获取验证码
 */
export function sigInEmailCode(params: any) {
  return http.request<API.BasicResponseModel>(
    {
      url: '/code/singIn',
      method: 'GET',
      params
    }
  );
}

/**
 * @description: 注册获取验证码
 */
export function forgetPasswordEmailCode(params: any) {
  return http.request<API.BasicResponseModel>(
    {
      url: '/code/password',
      method: 'GET',
      params
    }
  );
}

/**
 * @description: 登录获取验证码
 */
export function logInEmailCode(params: any) {
  return http.request<API.BasicResponseModel>(
    {
      url: '/code/logIn',
      method: 'GET',
      params
    }
  );
}

/**
 * @description: 用户密码登录
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
 * @description: 用户登录
 */
export function loginByEmail(params: any) {
  return http.request<API.BasicResponseModel>(
    {
      url: '/loginByEmail',
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

export enum UserInfoField {
  WORK = "work",
  hobby = "hobby",
}
interface IInfoParams {
  userId: number;
  fields?: Array<UserInfoField>
}
/**
 * @description: 用户信息
 */
export function getUserInfoById(params: IInfoParams) {
  return http.request<API.BasicResponseModel>(
    {
      url: '/user/infoByUserId',
      method: 'get',
      params,
    }
  );
}


/**
 * @description: 忘记密码
 */
export function forgetPassword(params: any) {
  return http.request<API.BasicResponseModel>(
    {
      url: '/forget',
      method: 'PUT',
      params,
    }
  );
}

/**
 * @description: 获取用户信息
 */
export function getUserInfo() {
  return http.request({
    url: '/user',
    method: 'get',
  });
}

/**
 * @description: 获取用户信息
 */

interface UserInfo {
  userId?: string | number | null
  petName?: string | null
  headIcon?: string | null
  gender?: number | null
  phone?: string | null

}
export function updateUserInfo(params: UserInfo) {
  return http.request<API.BasicResponseModel>({
    url: '/user',
    method: 'PUT',
    params
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

export function getSeal(id : number) {
  return http.request<API.BasicResponseModel>({
    url: '/sealNumber',
    method: 'GET',
    params: {id}
  },
    {
      isTransformResponse: false,
    }
  );
}
