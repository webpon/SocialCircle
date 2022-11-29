<template>
	<view>
		<!-- 我加入的圈子 -->
		<view v-if="joinTopicList.length > 0" class="join-topic-wrap">
			<view class="title">我加入的圈子</view>
			<scroll-view scroll-x>
				<view class="topic-list">
					<navigator class="topic-item" :url="'/pages/topic/detail?id='+item.id"
						v-for="(item,index) in joinTopicList" :key="item.id" hover-class="none">
						<image class="cover-img" :src="item.cover_image" mode="aspectFill"></image>
						<view class="name-text u-line-1">{{item.name}}</view>
					</navigator>
				</view>
			</scroll-view>
		</view>
		<!-- 热门圈子 -->
		<view v-else class="join-topic-wrap">
			<view class="title">热门圈子</view>
			<scroll-view scroll-x>
				<view class="topic-list">
					<navigator class="topic-item" :url="'/pages/topic/detail?id='+item.id"
						v-for="(item,index) in hotTopicList" :key="item.id" hover-class="none">
						<image class="cover-img" :src="item.cover_image" mode="aspectFill"></image>
						<view class="name-text u-line-1">{{item.name}}</view>
					</navigator>
				</view>
			</scroll-view>
		</view>
		<view style="height: 20rpx;background-color: #f5f5f5;"></view>
		<view style="margin: 20rpx;color: #999;">加入圈子的动态</view>
		<q-post-list ref="pWaterfall" :list="postList" :loadStatus="loadStatus"></q-post-list>
		<!-- 公告弹窗 -->
		<u-popup :customStyle="noticeStyle" :show="showNotice" mode="center" :round="10" :closeOnClickOverlay="true">
			<view class="notice-wrap">
				<view class="title">{{noticeInfo.title}}</view>
				<view class="content">{{noticeInfo.content}}</view>
				<view class="time">发布日期：{{noticeInfo.time | date}}</view>
				<view class="footer">
					<u-button text="知道了" @click="showNotice = false" :customStyle="{color:'#000'}" shape="circle"
						color="#ffc120"></u-button>
				</view>
			</view>
		</u-popup>
		<!-- tabbar -->
		<q-tabbar :active="0" :count="msgCount"></q-tabbar>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				loadStatus: 'loadmore',
				page: 1,
				postList: [],
				joinTopicList: [],
				hotTopicList: [],
				showNotice: false,
				noticeInfo: {},
				noticeStyle: {
					width: "90%"
				}
			};
		},
		computed: {
			msgCount() {
				return this.$store.state.messegeNum;
			}
		},
		onLoad() {
			this.getUserJoinTopic();
			this.getPostList();
			this.getNotice();
		},
		onShow() {
			if (this.$store.state.userInfo.id) {
				this.getMsgNum();
			}
		},
		onReachBottom() {
			this.page++;
			this.getPostList();
			this.getUserJoinTopic();
		},
		onPullDownRefresh() {
			this.page = 1;
			this.postList = [];
			this.getPostList();
			this.getUserJoinTopic();
			uni.stopPullDownRefresh();
		},
		methods: {
			getNotice() {
				this.$H.get('System/info', {
					key: 'notice'
				}).then(res => {
					if (res.result.value.content) {
						this.noticeInfo = res.result.value;
						this.showNotice = true;
					}
				});
			},
			//热门圈子
			getHotTopic() {
				this.$H.get('topic/hot', {
					count: 10
				}).then(res => {
					this.hotTopicList = res.result;
				});
			},
			// 用户加入的圈子列表
			getUserJoinTopic() {
				this.$H.post('topic/currentUserJoinTopic').then(res => {
					if (res.result.data.length > 0) {
						this.joinTopicList = res.result.data;
					} else {
						this.getHotTopic();
					}
				});
			},
			getMsgNum() {
				this.$H.post('message/num').then(res => {
					this.$store.state.messegeNum = [0, 0, 0, res.result.all_count, 0];
				});
			},
			// 获取帖子列表
			getPostList() {
				this.loadStatus = 'loading';
				this.$H
					.get('post/postListByjoinTopic', {
						page: this.page
					})
					.then(res => {
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
<style lang="scss" scoped>
	.join-topic-wrap {
		margin: 20rpx;

		.title {
			font-size: 28rpx;
			margin-bottom: 20rpx;
			color: #999;
		}

		.topic-list {
			display: flex;

			.topic-item {
				width: 120rpx;
				margin-right: 30rpx;

				.cover-img {
					width: 120rpx;
					height: 120rpx;
					display: block;
					border-radius: 10rpx;
					background-color: #eee;
				}

				.name-text {
					font-size: 28rpx;
					text-align: center;
				}
			}
		}
	}


	// 公告
	.notice-wrap {
		padding: 30rpx;

		.title {
			text-align: center;
			font-weight: bold;
			color: #616161;
			margin-bottom: 30rpx;
		}

		.content {
			line-height: 1.8;
			color: #616161;
			margin-bottom: 50rpx;
		}

		.time {
			color: #999;
			text-align: right;
			margin-bottom: 50rpx;
		}

		.footer {
			padding: 0 50rpx;
		}
	}
</style>
