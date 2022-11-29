import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		hasLogin: uni.getStorageSync("hasLogin"),
		userInfo:uni.getStorageSync("userInfo"),
		messegeNum:[0,0,0,0,0]
	},
	mutations: {
		login(state, userInfo) {			
			state.hasLogin = true;
			uni.setStorageSync("hasLogin",true);
			
			state.userInfo = userInfo;
			uni.setStorageSync("userInfo",userInfo);
		},
		logout(state) {
			state.hasLogin = false;
			uni.removeStorageSync("hasLogin");
			
			state.userInfo = null;
			uni.removeStorageSync("userInfo");
			
		}
	}
})

export default store
