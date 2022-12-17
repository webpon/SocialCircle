import { defineStore } from 'pinia';
import { createStorage } from '@/utils/Storage';
import { store } from '@/store';
import { ACCESS_TOKEN, CURRENT_USER } from '@/store/mutation-types';
import { ResultEnum } from '@/enums/httpEnum';
import {getUserInfo, login, doLogout, loginByEmail} from '@/api/system/user';
import { PageEnum } from '@/enums/pageEnum';
import router from '@/router';
import UserInfo from '@/type/UserInfo.type';

const Storage = createStorage({ storage: localStorage });

interface IUserState {
  token?: string;
  userInfo: Nullable<UserInfo>;
  lastUpdateTime: number;
}

interface LoginParams {
  email: string;
  password: string;
}

interface LoginByEmailParams {
  email: string;
  emailCode: string;
}

export const useUserStore = defineStore({
  id: 'app-user',
  state: (): IUserState => ({
    userInfo: null,
    token: undefined,
    lastUpdateTime: 0,
  }),
  getters: {
    getUserInfo(): UserInfo {
      return this.userInfo || Storage.get(CURRENT_USER, '') || {};
    },
    getToken(): string {
      return this.token || Storage.get(ACCESS_TOKEN, '');
    },
    getLastUpdateTime(): number {
      return this.lastUpdateTime;
    },
  },
  actions: {
    setToken(token: string | undefined) {
      this.token = token ? token : '';
      Storage.set(ACCESS_TOKEN, token);
    },
    setUserInfo(info: UserInfo | null) {
      if (this.$state.userInfo == null) {
        this.userInfo = info;
      }else {
        this.userInfo = {...info, ...this.$state.userInfo};
      }
      this.lastUpdateTime = new Date().getTime();
      Storage.set(CURRENT_USER, this.$state.userInfo);
    },

    async Login(params: LoginParams) {
      try {
        const response = await login(params);
        const { data, code } = response;
        if (code === ResultEnum.SUCCESS) {
          // save token
          this.setToken(data);
        }
        return Promise.resolve(response);
      } catch (error) {
        return Promise.reject(error);
      }
    },

    async LoginByEmailCode(params: LoginByEmailParams) {
      try {
        const response = await loginByEmail(params);
        const { data, code } = response;
        if (code === ResultEnum.SUCCESS) {
          // save token
          this.setToken(data);
        }
        return Promise.resolve(response);
      } catch (error) {
        return Promise.reject(error);
      }
    },

    async GetUserInfo() {
      return new Promise((resolve, reject) => {
        getUserInfo()
          .then((res) => {
            this.setUserInfo(res);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    async Logout() {
      if (this.getToken) {
        try {
          await doLogout();
        } catch {
          console.error('注销Token失败');
        }
      }
      this.setToken(undefined);
      this.setUserInfo(null);
      Storage.remove(ACCESS_TOKEN);
      Storage.remove(CURRENT_USER);
      router.push(PageEnum.BASE_LOGIN);
      location.reload();
    },
  },
});

// Need to be used outside the setup
export function useUserStoreWidthOut() {
  return useUserStore(store);
}
