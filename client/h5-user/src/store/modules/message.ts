import { defineStore } from 'pinia';
import { createStorage } from '@/utils/Storage';
import { store } from '@/store';
import { ACCESS_TOKEN, CURRENT_USER } from '@/store/mutation-types';
import { ResultEnum } from '@/enums/httpEnum';
import {getUserInfo, login, doLogout, loginByEmail} from '@/api/system/user';
import { PageEnum } from '@/enums/pageEnum';
import router from '@/router';
const Storage = createStorage({ storage: localStorage });
export interface IMessage {
  concernMsgShow: boolean
}
export const useMessageStore = defineStore({
  id: 'message',
  state: (): IMessage => ({
    concernMsgShow: false
   }),
  getters: {
    getConcernMsgShow(): boolean{
      return this.concernMsgShow;
    }
  },
  actions: {
   },
});

// Need to be used outside the setup
export function useUserStoreWidthOut() {
  return useMessageStore(store);
}
