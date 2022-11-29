<template>
	<view class="container">
		<q-field label="圈子名称" v-model="form.name" :required="true" placeholder="请填写圈子名称"></q-field>
		<q-field label="圈子介绍" type="textarea" v-model="form.description" :required="true" placeholder="请填写圈子介绍">
		</q-field>
		<view class="form-item">
			<view class="label">圈子头像</view>
			<q-alioss-upload :maxCount="1" :file="coverMediaList" ref="ossUpload" @success="coverSuccess" :autoUpload="true"></q-alioss-upload>
		</view>
		<view class="form-item">
			<view class="label">圈子背景</view>
			<q-alioss-upload :maxCount="1" :file="bgMediaList" ref="ossUpload" @success="bgSuccess" :autoUpload="true"></q-alioss-upload>
		</view>
		<!-- 提交按钮 -->
		<q-button shape="circle" @click="submit">提交</q-button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				sizeType: ['compressed'],
				form: {
					name: '',
					description: '',
					cover_image: '',
					bg_image: ''
				},
				coverMediaList: [],
				bgMediaList: []
			};
		},
		computed: {
			header() {
				return {
					token: this.$store.state.userInfo.token
				};
			}
		},
		onLoad(options) {
			this.getTopicInfo(options.id);
		},
		methods: {
			submit() {
				this.$refs.ossUpload.upload();
				this.$H.post('topic/topicSave', this.form).then(res => {
					if (res.code == 200) {
						this.$u.toast('保存成功');
					}
				});
			},
			coverSuccess(e) {
				this.form.cover_image = e[0].url;
			},
			bgSuccess(e) {
				this.form.bg_image = e[0].url;
			},
			getTopicInfo(topicId) {
				this.$H
					.get('topic/detail', {
						id: topicId
					})
					.then(res => {
						if (res.result.cover_image) {
							this.coverMediaList = [{
								url: res.result.cover_image
							}];
						}

						if (res.result.bg_image) {
							this.bgMediaList = [{
								url: res.result.bg_image
							}];
						}

						this.form = res.result;
					});
			}
		}
	};
</script>

<style>
	page {
		background-color: #f5f5f5;
	}
</style>
<style lang="scss" scoped>
	.form-item {
		background-color: #fff;
		padding: 30rpx;

		.label {
			margin-bottom: 10rpx;
		}
	}
</style>
