<template>
	<view>
		<view class="head-wrap">
			<view style="position: relative;z-index: 9">
				<u-navbar @leftClick="onBack" leftIconColor="#fff" :fixed="false" bgColor="unset"
					:border-bottom="false"></u-navbar>
			</view>
			<image class="bg-img" mode="aspectFill" :src="topicInfo.bg_image"></image>
			<view class="mask-bg"></view>
			<view class="head-content">
				<view class="topic-head">
					<image class="cover-img" mode="aspectFill" :src="topicInfo.cover_image"></image>
					<view class="name-wrap">
						<text class="name">{{ topicInfo.name }}</text>
						<text class="user">圈主 [{{ topicInfo.userInfo.username }}]</text>
					</view>
				</view>
				<view class="notice-txt u-line-2">{{ topicInfo.description | replace }}</view>
				<view class="user-wrap">
					<text><text style="margin-right: 10rpx;">{{ topicInfo.user_num }}</text>圈友已加入</text>
					<navigator :url="'/pages/topic/user-list?id='+topicId" class="right" hover-class="none">
						<u-avatar-group :urls="avatarUrls" size="20" gap="0.3"></u-avatar-group>
						<u-icon name="arrow-right" color="#fff"></u-icon>
					</navigator>
				</view>
			</view>
			<view class="tool-menu-wrap">
				<navigator class="m-item" :url="'/pages/topic/setting?id=' + topicId">
					<u-icon name="/static/set.png" :size="18"></u-icon>
				</navigator>
				<view class="m-item" @click="showShare = true">
					<u-icon name="/static/share_1.png" :size="18"></u-icon>
				</view>
			</view>
		</view>
		<view class="content-main">
			<!-- 置顶 -->
			<view class="post-top-box" v-if="topicInfo.top_post && topicInfo.top_post.length > 0">
				<navigator @longpress="onTopDel(item, index)" class="post-item"
					:url="'/pages/post/detail?id=' + item.id" v-for="(item, index) in topicInfo.top_post" :key="index">
					<view class="tag">置顶</view>
					<view class="title">{{ item.title || item.content.substring(0, 15) }}</view>
				</navigator>
			</view>
			<!-- 分类tab -->
			<view class="tab-wrap">
				<u-tabs :list="calssList" lineColor="#000" :activeStyle="tabActiveStyle"
					:inactiveStyle="tabInactiveStyle" :is-scroll="false" :current="current" @change="tabsChange">
				</u-tabs>
			</view>
			<q-post-list :showTopic="false" :handle="true" :uid="topicInfo.uid" :list="postList"
				:loadStatus="loadStatus" :admin="topicInfo.is_admin"></q-post-list>
		</view>
		<!-- 加入圈子弹窗 -->
		<u-modal :show="joinTopicModel" :show-cancel-button="true" @cancel="joinTopicModel = false" confirm-text="加入圈子"
			:content="'是否加入【' + topicInfo.name + '】?'" @confirm="joinTopic"></u-modal>
		<!-- 选择分享弹窗 -->
		<u-popup :show="showShare" mode="bottom" border-radius="20" @close="showShare = false">
			<view class="share-type">
				<button open-type="share" class="type-item u-reset-button">
					<u-icon class="icon" name="weixin-fill" color="#00b33c" size="40rpx"></u-icon>
					<text>发给微信好友</text>
				</button>
				<navigator class="type-item" :url="'/pages/topic/qrcode?id='+topicId" hover-class="none">
					<u-icon class="icon" name="grid" color="#000" size="40rpx"></u-icon>
					<text>分享二维码</text>
				</navigator>
			</view>
		</u-popup>
		<!-- 发布按钮 -->
		<view @click="handelAdd" class="plus-btn">
			<u-icon name="plus" color="#fff"></u-icon>
			<text class="text">发布动态</text>
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
				tabActiveStyle: {
					fontWeight: 'bold',
					fontSize: '35rpx'
				},
				tabInactiveStyle: {
					fontWeight: 'bold',
					fontSize: '35rpx',
					color: "#999"
				},
				showPlusPost: false,
				showShare: false,
				joinTopicModel: false,
				current: 0,
				calssList: [],
				topicId: 0,
				topicInfo: {},
				postList: [],
				loadStatus: 'loading',
				page: 1,
				avatarUrls: [],
				classId: ''
			};
		},
		onLoad(options) {

			if (options.scene) {
				this.topicId = options.scene;
			} else {
				this.topicId = options.id;
			}

			this.getTopicInfo();
			this.getTopicClass();
			this.getUserList();
			this.getPostList();
		},
		onReachBottom() {
			this.page++;
			this.getPostList();
		},
		onPullDownRefresh() {
			this.page = 1;
			this.postList = [];

			this.getTopicClass();
			this.getPostList();
			this.getTopicInfo();
			uni.stopPullDownRefresh();
		},
		onShareAppMessage(res) {
			if (res.from === 'button') {
				// 来自页面内分享按钮
				console.log(res.target);
			}
			return {
				title: this.topicInfo.name + '-' + this.topicInfo.description,
				path: '/pages/topic/detail?id=' + this.topicId,
				imageUrl: this.topicInfo.bg_image
			};
		},
		onShareTimeline() {
			return {
				title: this.topicInfo.name + '-' + this.topicInfo.description,
				imageUrl: this.topicInfo.bg_image,
				query: 'id=' + this.topicId
			};
		},
		filters: {
			substr: function(e) {
				return e.substr(0, 5);
			},
			replace(str) {
				if (str) {
					str = str.replace(/\n/g, '');
				}

				return str;
			}
		},
		methods: {
			getTopicClass() {
				this.$H.get('topic/postClassList', {
					topic_id: this.topicId
				}).then(res => {
					let calssList = [{
						id: '',
						name: '推荐'
					}]

					this.calssList = [...calssList, ...res.result];
					
				});
			},
			getUserList() {
				this.$H.get('topic/userList', {
					page: this.page,
					id: this.topicId
				}).then(res => {
					for (let i = 0; i < 3; i++) {
						let user = res.result.data;
						this.avatarUrls.push(user[i].avatar)
					}
				});
			},
			onTopDel(e, index) {
				let that = this;
				uni.showModal({
					title: '提示',
					content: '是否解除置顶?',
					success: function(res) {
						if (res.confirm) {
							that.$H
								.post('post/topPostDel', {
									post_id: e.id,
									topic_id: e.topic_id
								})
								.then(res => {
									if (res.code == 200) {
										that.topicInfo.top_post.splice(index, 1);
									}
								});
						} else if (res.cancel) {
							// console.log('用户点击取消');
						}
					}
				});
			},
			onBack() {
				let pages = getCurrentPages();
				if (pages.length > 1) {
					uni.navigateBack();
				} else {
					uni.switchTab({
						url: '/pages/index/index'
					});
				}
			},
			tabsChange(e) {
				let index = e.index;

				this.classId = e.id;
				this.current = index;
				this.page = 1;
				this.postList = [];

				this.getPostList();
			},
			onTrigger(type) {
				if (!this.topicInfo.is_join) {
					this.joinTopicModel = true;
					return;
				}

				this.showPlusPost = false;
				if (type == 3) {
					uni.navigateTo({
						url: '/pages/post/vote?topic_id=' + this.topicId + "&topic_name=" + this.topicInfo.name
					});
				} else {
					uni.navigateTo({
						url: '/pages/post/add?topic_id=' + this.topicId + '&topic_name=' + this.topicInfo.name +
							'&type=' + type
					});
				}
			},
			handelAdd() {
				if (this.$store.state.hasLogin) {
					if (this.topicInfo.is_join) {
						this.showPlusPost = true;
					} else {
						this.joinTopicModel = true;
					}
				} else {
					uni.navigateTo({
						url: '/pages/user/login'
					});
				}
			},
			joinTopic() {
				this.$H
					.post('topic/joinTopic', {
						id: this.topicId
					})
					.then(res => {
						if (res.code === 200) {
							this.topicInfo.is_join = true;
							this.joinTopicModel = false;
						}
					});
			},
			getTopicInfo() {
				this.$H
					.get('topic/detail', {
						id: this.topicId
					})
					.then(res => {
						this.topicInfo = res.result;
					});
			},
			getPostList() {
				this.loadStatus = 'loading';

				this.$H.get('post/list', {
					topic_id: this.topicId,
					class_id: this.classId,
					page: this.page
				}).then(res => {
					this.postList = this.postList.concat(res.result.data);

					if (res.result.current_page === res.result.last_page || res.result.last_page === 0) {
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
	.head-wrap {
		height: 650rpx;
		position: relative;

		.bg-img {
			position: absolute;
			left: 0;
			right: 0;
			bottom: 0;
			top: 0;
			width: 100%;
			height: 100%;
			filter: blur(3px);
		}

		.mask-bg {
			position: absolute;
			left: 0;
			right: 0;
			bottom: 0;
			top: 0;
			width: 100%;
			height: 100%;
			background-image: linear-gradient(rgba(0, 0, 0, 0.2), rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.9));
		}

		.head-content {
			position: relative;
			color: #eee;
			padding: 20rpx;

			.topic-head {
				display: flex;
				margin-bottom: 30rpx;

				.cover-img {
					width: 100rpx;
					height: 100rpx;
					border-radius: 10rpx;
					margin-right: 20rpx;
				}

				.name-wrap {
					display: flex;
					flex-direction: column;

					.name {
						font-size: 32rpx;
						font-weight: bold;
					}

					.user {
						font-size: 24rpx;
						color: #999;
					}
				}
			}

			.notice-txt {
				margin-bottom: 30rpx;
				font-size: 28rpx;
			}

			.user-wrap {
				display: flex;
				align-self: center;
				margin-bottom: 30rpx;

				.right {
					margin-left: auto;
					display: flex;
					align-self: center;
				}
			}
		}
	}

	.tab-wrap {
		margin: 20rpx;
	}

	.content-main {
		background-color: #fff;
		position: absolute;
		width: 100%;
		top: 620rpx;
		min-height: 80vh;
		border-radius: 30rpx;
	}

	// 置顶
	.post-top-box {
		background-color: #fff;
		padding: 20rpx;

		.post-item {
			display: flex;
			align-items: center;
			padding: 20rpx;
			font-size: 28rpx;

			&:last-child {
				margin-bottom: 0;
			}

			.tag {
				background-color: #333;
				color: #fff;
				padding: 0 10rpx;
				border-radius: 10rpx;
				font-size: 20rpx;
				height: 40rpx;
				line-height: 40rpx;
				margin-right: 20rpx;
			}
		}
	}

	// 分享类型弹窗
	.share-type {
		padding: 50rpx 30rpx;

		.type-item {
			background-color: #f5f5f5;
			padding: 20rpx;
			display: flex;
			justify-content: center;
			align-items: center;

			.icon {
				width: 40rpx;
				height: 40rpx;
				margin-right: 20rpx;
			}

			&:nth-child(2) {
				margin: 20rpx 0;
			}
		}
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

	.tool-menu-wrap {
		display: flex;
		justify-content: flex-end;
		position: absolute;
		left: 0;
		right: 0;
		bottom: 80rpx;
		padding: 0 30rpx;

		.m-item {
			padding: 15rpx;
			background-color: $themes-color;
			margin-left: 20rpx;
			border-radius: 50%;
			display: flex;
			justify-content: center;
			align-items: center;
		}
	}

	.plus-btn {
		position: fixed;
		bottom: 50rpx;
		background-color: $themes-color;
		display: flex;
		align-self: center;
		justify-content: center;
		border-radius: 100rpx;
		line-height: 1;
		padding: 20rpx 40rpx;
		color: #fff;
		margin-left: 38%;
		font-size: 28rpx;

		.text {
			margin-left: 10rpx;
		}
	}
</style>
