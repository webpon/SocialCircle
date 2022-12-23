import { defineStore } from 'pinia';
import { createStorage } from '@/utils/Storage';
import { store } from '@/store';
import { ACCESS_TOKEN, CURRENT_USER } from '@/store/mutation-types';
import { ResultEnum } from '@/enums/httpEnum';
import {getUserInfo, login, doLogout, loginByEmail} from '@/api/system/user';
import { PageEnum } from '@/enums/pageEnum';
import router from '@/router';
const Storage = createStorage({ storage: localStorage });
interface TopicNum {
  topicId?: number;
  msgNum?: number;
}
export interface IMessage {
  concernMsgShow: boolean;
  topicItemNum: Array<TopicNum>;
}

export const useMessageStore = defineStore({
  id: 'message',
  state: (): IMessage => ({
    concernMsgShow: false,
    topicItemNum: [{
      topicId:4,
      msgNum: 100
    }]
   }),
  getters: {
    getTopicNum():Array<TopicNum> {
      return this.topicItemNum;
    },
    getConcernMsgShow(): boolean{
      return this.concernMsgShow;
    }
  },
  actions: {
    setTopicNum(topic, num: number){
      console.log(topic)
      const t = this.topicItemNum.map(item => {
        const {id} = topic;
        console.log(id)
        if (item.topicId === id){
          item.msgNum = num;
        }
      })
      console.log(t)
    }
   },
});

// Need to be used outside the setup
export function useMsgStoreWidthOut() {
  return useMessageStore(store);
}
