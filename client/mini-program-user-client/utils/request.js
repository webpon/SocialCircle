import config from './config.js';
import store from '../store/index.js';
export default {
	request(options = {}) {
		return new Promise((resolve, reject) => {
			let url = options.url;
			if (url.indexOf("http://") == -1 && url.indexOf("https://") == -1) {
				options.url = config.domain + url;
			}

			if (store.state.userInfo) {
				options.header.token = store.state.userInfo.token;
			}
			options.complete = (response) => {
				if (response.statusCode == 200) {
					let code = response.data.code;

					if (code == 500) {
						uni.showToast({
							title: response.data.msg,
							icon: "none"
						});
					}

					resolve(response.data)
				} else if (response.statusCode == 420) {
					uni.navigateTo({
						url: "/pages/user/login"
					})
				}
			}

			uni.request(options)
		})
	},

	post(url, data = {}, header = {}) {

		let options = {
			url: url,
			data: data,
			header: header,
			method: "POST"
		}

		return this.request(options);
	},

	get(url, data = {}, header = {}) {
		let options = {
			url: url,
			data: data,
			header: header
		}

		return this.request(options);
	}
};
