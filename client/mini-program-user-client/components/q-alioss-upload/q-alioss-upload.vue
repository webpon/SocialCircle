<template>
	<view>
		<block v-if="isAvatar">
			<image class="avatar-img" @click="chooseImage" :src="avatarSrc" mode="aspectFill"></image>
		</block>
		<block v-else>
			<u-upload :accept="accept" :fileList="fileList" @afterRead="afterRead" @delete="deletePic" multiple
				:maxCount="maxCount"></u-upload>
		</block>
	</view>
</template>

<script>
	const base64 = require('./base64.js');
	require('./hmac.js');
	require('./sha1.js');
	const Crypto = require('./crypto.js');

	export default {
		name: "q-alioss-upload",
		props: {
			accept: {
				type: String,
				default: "image"
			},
			maxCount: {
				type: Number,
				default: 9
			},
			autoUpload: {
				type: Boolean,
				default: false
			},
			file: {
				type: Array,
				default: []
			},
			isAvatar: false,
			avatarSrc:String
		},
		data() {
			return {
				uploadImageUrl: 'https://q-meoyun-com.oss-cn-shenzhen.aliyuncs.com/',
				AccessKeySecret: null,
				OSSAccessKeyId: null,
				securityToken: null,
				timeout: 87600,
				fileList: []
			};
		},
		watch: {
			file(val) {
				this.fileList = val
			}
		},
		created() {
			this.initOssInfo();
		},
		methods: {
			initOssInfo() {
				this.$H.get("AliCloud/getAuth").then(res => {
					this.OSSAccessKeyId = res.result.Credentials.AccessKeyId;
					this.AccessKeySecret = res.result.Credentials.AccessKeySecret;
					this.securityToken = res.result.Credentials.SecurityToken;
				})
			},
			chooseImage() {
				let that = this;
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album'],
					success: async function(res) {
						const result = await that.uploadFilePromise(res.tempFilePaths[0])
						that.$emit("success", result)
					}
				});
			},
			//上传操作
			async upload() {
				let lists = this.fileList;

				for (let i = 0; i < lists.length; i++) {
					this.fileList[i].status = 'uploading';
					this.fileList[i].message = '上传中';
				}

				for (let i = 0; i < lists.length; i++) {
					const result = await this.uploadFilePromise(lists[i].url)

					this.fileList.splice(i, 1, Object.assign(this.fileList[i], {
						status: 'success',
						message: '',
						url: result
					}))
				}

				this.$emit("success", this.fileList)
			},
			// 删除图片
			deletePic(event) {
				this.fileList.splice(event.index, 1)
			},
			// 新增图片
			async afterRead(event) {
				// 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
				let lists = [].concat(event.file)
				lists.map((item) => {
					this.fileList.push({
						...item,
						status: '',
						message: ''
					})
				})

				if (this.autoUpload) {
					this.upload();
				}
			},
			uploadFilePromise(url) {
				let that = this;
				return new Promise((resolve, reject) => {
					const accessid = this.OSSAccessKeyId;
					const policyBase64 = this.getPolicyBase64();
					const signature = this.getSignature(policyBase64); //获取签名

					let date = new Date();
					let year = date.getFullYear();
					let month = date.getMonth() + 1;
					let day = date.getDate();
					let suffix = url.substring(url.lastIndexOf("."));
					const fileKey = year + "/" + month + "/" + day + "/" + uni.$u.guid(20, false) + suffix;

					let a = uni.uploadFile({
						url: this.uploadImageUrl,
						filePath: url,
						name: 'file',
						formData: {
							'key': fileKey,
							'policy': policyBase64,
							'OSSAccessKeyId': accessid,
							'signature': signature,
							'x-oss-security-token': that.securityToken,
							'success_action_status': '200',
						},
						success: (res) => {
							if (res.statusCode == 200) {
								resolve(this.uploadImageUrl + fileKey)
							}
						}
					});
				})
			},
			getPolicyBase64() {
				let date = new Date();
				date.setHours(date.getHours() + this.timeout);
				let srcT = date.toISOString();
				const policyText = {
					"expiration": srcT, //设置该Policy的失效时间，超过这个失效时间之后，就没有办法通过这个policy上传文件了 
					"conditions": [
						["content-length-range", 0, 100 * 1024 * 1024] // 设置上传文件的大小限制,500MB
					]
				};

				const policyBase64 = base64.encode(JSON.stringify(policyText));
				return policyBase64;
			},
			getSignature(policyBase64) {
				const accesskey = this.AccessKeySecret;

				const bytes = Crypto.HMAC(Crypto.SHA1, policyBase64, accesskey, {
					asBytes: true
				});
				const signature = Crypto.util.bytesToBase64(bytes);
				return signature;
			}
		}
	}
</script>

<style lang="scss">
	.img-wrap {
		display: grid;
		grid-template-columns: repeat(3, 1fr);
		grid-gap: 20rpx;

		.img-item {
			height: 200rpx;

			.img {
				width: 100%;
				height: 100%;
			}
		}

		.upload-add {
			background-color: #eee;
			display: flex;
			justify-content: center;
			align-items: center;
			height: 200rpx;
		}
	}

	.avatar-img {
		width: 100rpx;
		height: 100rpx;
		background-color: #999;
		border-radius: 100rpx;
	}
</style>
