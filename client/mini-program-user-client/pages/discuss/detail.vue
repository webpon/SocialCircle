<template>
	<view>
		<view class="dis-title">
			<text class="tag">#</text>
			<text class="title">{{ discussInfo.title }}</text>
		</view>
		<view class="discussInfo">
			<view class="count">
				<!-- <text>{{ discussInfo.post_num }} 篇内容</text> -->
				<text>{{ discussInfo.see_num }} 次浏览</text>
			</view>
			<!-- <view class="discuss-desc">{{ discussInfo.introduce }}</view> -->
		</view>
		<q-post-list :list="postList" :loadStatus="loadStatus"></q-post-list>
		<!-- 发布按钮 -->
		<view @click="handelAdd" class="plus-box">
			<u-icon color="#fff" name="plus"></u-icon>
		</view>
		<!-- 发布弹窗 -->
		<u-popup :show="showPlusPost" mode="bottom" border-radius="20" width="80%">
			<view class="post-add-popup">
				<view @click="onTrigger(1)" class="p-item">
					<image mode="widthFix" class="icon" src="/static/h_2.png"></image>
					<text class="txt">发布帖子</text>
				</view>
				<view @click="onTrigger(2)" class="p-item">
					<image mode="widthFix" class="icon" src="/static/h_3.png"></image>
					<text class="txt">发布视频</text>
				</view>
				<view @click="onTrigger(3)" class="p-item">
					<image mode="widthFix" class="icon" src="/static/h_1.png"></image>
					<text class="txt">发布投票</text>
				</view>
			</view>
			<view class="handle-close">
				<u-icon @click="showPlusPost = false" name="close"></u-icon>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				showPlusPost: false,
				discussTitle: '',
				loadStatus: 'loading',
				postList: [],
				discussInfo: {},
				page: 1
			};
		},
		onLoad(options) {
			this.discussTitle = options.title;
			this.getDiscussInfo();

			//#ifdef MP-WEIXIN
			wx.showShareMenu({
				withShareTicket: true,
				menus: ['shareAppMessage', 'shareTimeline']
			});
			//#endif
		},
		onShareAppMessage(res) {
			if (res.from === 'button') {
				// 来自页面内分享按钮
				console.log(res.target);
			}
			return {
				title: this.discussInfo.title,
				path: '/pages/discuss/detail?title=' + this.discussInfo.title
			};
		},
		onShareTimeline() {
			let imgURL = '';
			return {
				title: this.discussInfo.title,
				imageUrl: imgURL,
				query: 'title=' + this.discussInfo.title
			};
		},
		methods: {
			handelAdd() {
				if (this.$store.state.hasLogin) {
					this.showPlusPost = true;
				} else {
					uni.navigateTo({
						url: '/pages/user/login'
					});
				}
			},
			onTrigger(type) {
				this.showPlusPost = false;
				if (type == 3) {
					uni.navigateTo({
						url: '/pages/post/vote'
					});
				} else {
					uni.navigateTo({
						url: '/pages/post/add?type=' + type + '&title=' + this.discussInfo.title
					});
				}
			},
			getDiscussInfo() {
				this.$H.get('discuss/infoByTitle', {
					title: this.discussTitle
				}).then(res => {
					this.discussInfo = res.result;
					this.getPostList();
				});
			},
			getPostList() {
				this.loadStatus = 'loading';
				this.$H
					.post('post/list', {
						discuss_title: this.discussInfo.title,
						page: this.page
					})
					.then(res => {
						this.postList = this.postList.concat(res.result.data);

						if (res.current_page === res.last_page || res.last_page === 0) {
							this.loadStatus = 'nomore';
						} else {
							this.loadStatus = 'loadmore';
						}
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
	.dis-title {
		background-color: #fff;
		padding: 0 30rpx;
		line-height: 1;

		.tag {
			display: inline-flex;
			justify-content: center;
			align-items: center;
			width: 45rpx;
			height: 45rpx;
			border-radius: 50%;
			background-color: #000;
			color: #fff;
			font-weight: bold;
			margin-right: 10rpx;
			font-size: 24rpx;
		}

		.title {
			font-weight: bold;
			color: #000000;
			font-size: 38rpx;
		}
	}

	.discussInfo {
		padding: 20rpx;
		margin-bottom: 20rpx;
		background-color: #fff;
		align-items: center;
	}

	.avatar {
		margin-right: 10rpx;
	}

	.count {
		font-size: 12px;
		color: #999;
		margin-bottom: 10rpx;
	}

	.count text {
		margin: 0 10rpx;
	}

	.discuss-desc {
		color: #616161;
	}

	// 发布弹窗
	.post-add-popup {
		display: flex;
		padding: 50rpx 0;

		.p-item {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			flex: 1;
			padding: 20rpx 0;
			border-radius: 20rpx;

			.icon {
				width: 100rpx;
				margin-bottom: 20rpx;
			}

			.txt {
				font-size: 28rpx;
			}
		}
	}

	.handle-close {
		display: flex;
		justify-content: center;
		margin-bottom: 50rpx;
	}
</style>
