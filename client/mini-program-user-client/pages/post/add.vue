<template>
	<view>
		<view class="container">
			<!-- 选择圈子 -->
			<navigator url="/pages/choose-topic/choose-topic" class="choose-item">
				<image class="icon" src="/static/p_1.png"></image>
				<text class="txt">{{ topicName }}</text>
				<view style="margin-left: auto;">
					<u-icon name="arrow-right"></u-icon>
				</view>
			</navigator>
			<!-- 选择板块分区 -->
			<view class="class-wrap">
				<view class="title">板块分区</view>
				<scroll-view scroll-x style="width: calc(100vw - 180rpx);">
					<view class="class-list">
						<view class="class-item" :class="index == classIndex?'class-active':''"
							@click="chooseClass(item,index)" v-for="(item,index) in calssList" :key="index">
							{{item.name}}
						</view>
					</view>
				</scroll-view>
			</view>
			<textarea placeholder="这一刻的想法..." @blur="inputBlur" :focus="inputFocus" :auto-height="true"
				:show-confirm-bar="false" maxlength="-1" v-model="form.content" class="post-txt"></textarea>
			<!-- 上传图片 -->
			<q-alioss-upload v-if="form.type == 1" ref="ossUpload" @success="onSubmit"></q-alioss-upload>
			<!-- 上传视频 -->
			<q-alioss-upload v-if="form.type == 2" accept='video' :maxCount="1" ref="ossUpload" @success="onSubmit">
			</q-alioss-upload>

			<!-- 所在位置 -->
			<view @click="chooseLocation" class="choose-location">
				<u-icon name="map" size="30rpx"></u-icon>
				<text class="txt">{{ form.address || '你在哪里?' }}</text>
			</view>
		</view>
		<!-- 工具栏 -->
		<view class="tool-wrap">
			<view @click="showDiscuss = true" class="tool-item">
				<image class="icon" src="/static/m_1.png"></image>
				<text class="txt">选择话题</text>
			</view>
			<view @click="onUpload" class="submit-btn">发布</view>
		</view>
		<!-- 选择话题弹窗 -->
		<u-popup :show="showDiscuss" @close="closePopup" @open="getDiscussList" :round="10" :closeable="true">
			<view class="discuss-popup">
				<div class="dis-wrap">
					<u-input placeholder="请输入话题名称" v-model="inputDiscussName" border="bottom" @change="getDiscussList"
						clearable>
						<div slot="prefix" class="dis-prefix-icon">#</div>
					</u-input>
				</div>
				<div style="color: #999;margin: 30rpx;">热议话题</div>
				<view class="discuss-item" @click="chooseDiscuss(item)" v-for="(item, index) in discussList"
					:key="index">
					#{{ item.title}}#
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				mediaList: [],
				topicName: '选择圈子',
				form: {
					title: '',
					type: 1,
					topic_id: '',
					class_id: '',
					discuss_list: [],
					content: '',
					media: [],
					longitude: 0,
					latitude: 0,
					address: ''
				},
				showDiscuss: false,
				discussList: [],
				inputCursor: 0,
				inputFocus: true,
				inputDiscussName: '',
				calssList: [],
				classIndex: null
			};
		},
		watch: {
			'form.topic_id'(val) {
				this.getTopicClass();
			}
		},
		onLoad(options) {

			if (options.type) {
				this.form.type = options.type;
			}

			if (options.topic_id) {
				this.form.topic_id = options.topic_id;
				this.topicName = options.topic_name;
			}

			if (options.title) {
				this.form.content = '#' + options.title + '#'
			}

			let location = uni.getStorageSync('location_info');
			this.form.longitude = location.longitude;
			this.form.latitude = location.latitude;

			this.getTopicClass();
		},
		methods: {
			chooseClass(e, index) {
				this.classIndex = index;
				this.form.class_id = e.id;
			},
			getTopicClass() {
				this.$H.get('topic/postClassList', {
					topic_id: this.form.topic_id
				}).then(res => {
					this.calssList = res.result;
				});
			},
			onSubmit(e) {
				let fileList = [];
				e.forEach(item => {
					fileList.push(item.url)
				})

				let discussList = this.form.content.match(/#(.*?)#/g);

				if (discussList) {
					discussList.forEach(item => {
						item = item.replace(/#/g, '');
						this.form.discuss_list.push(item);
					})
				}

				this.form.content = this.form.content.replace(/#(.*?)#/g, '');

				this.form.media = fileList;

				this.$H.post('post/release', this.form).then(res => {
					if (res.code == 200) {
						uni.redirectTo({
							url: '/pages/post/detail?id=' + res.result.id
						});

						this.$H.post('post/checkPostImg', {
							post_id: res.result.id
						});
					}
					uni.hideLoading();
				});
			},
			inputBlur(event) {
				this.inputCursor = event.detail.cursor;
				this.inputFocus = false;
			},
			chooseDiscuss(e) {
				let discussText = "  #" + e.title + "#  ";
				let contentText = this.form.content;

				this.form.content = discussText + contentText;

				this.showDiscuss = false;
				this.inputFocus = true;
				this.inputDiscussName = '';
			},
			getDiscussList(keyword) {

				let form = {
					page: this.page,
				};

				if (this.topicId) {
					form.topic_id = this.topicId
				}

				if (keyword) {
					form.keyword = keyword
				}

				this.$H.get('discuss/list', form).then(res => {
					this.discussList = res.result.data;

					if (res.result.data.length == 0) {
						if (keyword) {
							this.discussList.push({
								title: keyword
							})
						}

					}
				});
			},
			closePopup() {
				this.showDiscuss = false;
				this.inputDiscussName = '';
			},
			onUpload() {
				if (!this.form.content) {
					this.$u.toast('内容不能为空');
					return;
				}

				if (!this.form.topic_id) {
					this.$u.toast('请选择圈子');
					return;
				}
				this.$refs.ossUpload.upload();

				uni.showLoading({
					mask: true,
					title: '发布中'
				});
			},
			chooseLocation() {
				let that = this;
				uni.chooseLocation({
					success: function(res) {
						// console.log('位置名称：' + res.name);
						// console.log('详细地址：' + res.address);
						// console.log('纬度：' + res.latitude);
						// console.log('经度：' + res.longitude);
						that.form.address = res.name;
						that.form.latitude = res.latitude;
						that.form.longitude = res.longitude;
					}
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
	.post-txt {
		width: 100%;
		padding: 20rpx 0;
		min-height: 300rpx;
	}

	.choose-item {
		display: flex;
		align-items: center;
		padding: 20rpx 0;
		border-bottom: 1px solid #f5f5f5;

		&:last-child {
			border: 0;
		}

		.txt {
			margin-left: 20rpx;
		}

		.icon {
			width: 40rpx;
			height: 40rpx;
		}

		.icon-right {
			margin-left: 0;
		}
	}

	.choose-location {
		display: inline-flex;
		align-items: center;
		background-color: #eee;
		font-size: 28rpx;
		border-radius: 100rpx;
		padding: 10rpx 20rpx;
		line-height: 1;
		color: #616161;

		.txt {
			margin-left: 10rpx;
		}
	}

	.tool-wrap {
		display: flex;
		background-color: #f5f5f5;
		padding: 30rpx;
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;

		.tool-item {
			display: flex;
			justify-content: center;
			align-items: center;
			background-color: #fff;
			font-size: 28rpx;
			padding: 10rpx 20rpx;
			line-height: 1;
			border-radius: 100rpx;

			.icon {
				width: 30rpx;
				height: 30rpx;
				display: block;
				margin-right: 10rpx;
			}
		}

		.submit-btn {
			margin-left: auto;
			font-size: 28rpx;
			background-color: $themes-color;
			padding: 15rpx 30rpx;
			border-radius: 100rpx;
			line-height: 1;
			color: #fff;
		}
	}

	.discuss-popup {
		min-height: 70vh;
		padding-top: 50rpx;

		.dis-wrap {
			margin: 30rpx;

			.dis-prefix-icon {
				color: #999;
				font-weight: bold;
				margin: 0 20rpx;
			}
		}

		.discuss-item {
			margin: 30rpx;
			font-weight: bold;
		}
	}

	.class-wrap {
		display: flex;
		margin: 30rpx 0;

		.title {
			font-size: 28rpx;
			color: #999;
			margin-right: 20rpx;
			white-space: nowrap;
		}

		.class-list {
			display: flex;
			align-self: center;

			.class-item {
				margin-right: 20rpx;
				background-color: #eee;
				font-size: 24rpx;
				line-height: 1;
				padding: 10rpx 20rpx;
				border-radius: 100rpx;
				white-space: nowrap;
			}
			
			.class-active {
				background-color: $themes-color;
				color: #fff;
			}
		}
	}
</style>
