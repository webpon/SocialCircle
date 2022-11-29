<template>
	<view>
		<view style="position: absolute;">
			<u-navbar @leftClick="onBack" bgColor="unset"></u-navbar>
		</view>
		<view class="head-bg"></view>
		<view class="con-c">
			<view class="user-info">
				<u-avatar :customStyle="{'z-index':99}" :src="userInfo.avatar" size="100"></u-avatar>
				<view class="user-c">
					<view class="username">
						<text>{{ userInfo.username }}</text>
					</view>
					<view class="num-box">
						<text>
							{{ userInfo.follow_num }}
							<text class="txt">关注</text>
						</text>
						<text>
							{{ userInfo.fans_num }}
							<text class="txt">粉丝</text>
						</text>
						<text class="level">{{ userInfo.level }}</text>
					</view>
					<text class="desc u-line-2">{{ userInfo.intro }}</text>
					<view class="btn-box">
						<view v-if="!userInfo.is_follow" @click="follow" class="follow-btn">
							<u-icon name="plus" color="#fff"></u-icon>
							<text class="text">关注</text>
						</view>
						<view v-else @click="cancelFollow" class="follow-btn" style="background-color: #eee;">
							<text class="text">已关注</text>
						</view>
					</view>
				</view>
			</view>
			<!-- tab -->
			<view class="tab-box">
				<u-tabs lineColor="#000" keyName="tab_name" :list="tabs" :is-scroll="false" :current="current"
					@change="tabChange"></u-tabs>
			</view>
			<!-- 主页 -->
			<view v-show="current === 0">
				<!-- 基本信息 -->
				<view class="f-wrap u-skeleton-fillet">
					<view class="title">基本信息</view>
					<view class="info-c">
						<view v-if="userInfo.type === 9" class="level-box">
							<view class="level">
								<u-icon name="level" color="#fff"></u-icon>
							</view>
							<text>官方账号</text>
						</view>
						<text v-if=" userInfo.gender">性别：{{ userInfo.gender }}</text>
						<text v-if="userInfo.age">年龄：{{ userInfo.age }}岁</text>
						<text v-if="userInfo.province">地区：{{ userInfo.province }} {{ userInfo.city }}</text>
						<text>个人简介：{{ userInfo.intro }}</text>
					</view>
				</view>
				<!-- 创建的圈子 -->
				<view v-if="userInfo.create_topic_list.length > 0" class="f-wrap u-skeleton-fillet">
					<view class="title">创建的圈子</view>
					<q-topic-list :list="userInfo.create_topic_list" loadStatus="none"></q-topic-list>
				</view>
				<!-- 个人标签 -->
				<view v-if="userInfo.tag_str.length > 0" class="f-wrap u-skeleton-fillet">
					<view class="title">标签</view>
					<view class="tag-box">
						<view v-for="(item, index) in userInfo.tag_str" :key="index" class="tag">{{ item }}</view>
					</view>
				</view>
			</view>
			<!-- 帖子 -->
			<view v-show="current === 1">
				<q-post-list :list="postList" :loadStatus="loadStatus"></q-post-list>
			</view>
			<!-- 加入的圈子 -->
			<view v-show="current === 2">
				<q-topic-list :list="topicList"></q-topic-list>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				loading: true,
				btnStyle: {
					color: '#fff',
					backgroundColor: '#efd234'
				},
				background: {
					backgroundColor: 'unset'
				},
				tabs: [{
						tab_name: '主页'
					},
					{
						tab_name: '帖子'
					},
					{
						tab_name: '加入圈子'
					}
				],
				current: 0,
				userId: null,
				postList: [],
				topicList: [],
				userInfo: {
					create_topic_list: []
				},
				loadStatus: 'loading',
				page: 1
			};
		},
		onLoad(options) {
			this.userId = options.user_id;
			this.getUserInfo();
		},
		onReachBottom() {
			if (this.current == 1) {
				this.page++;
				this.getPostList();
			}
		},
		methods: {
			onBack() {
				uni.navigateBack();
			},
			follow() {
				this.$H
					.post('user/addFollow', {
						user_id: this.userInfo.id
					})
					.then(res => {
						if (res.code === 200) {
							this.userInfo.is_follow = true;
						}
					});
			},
			cancelFollow() {
				this.$H
					.post('user/cancelFollow', {
						user_id: this.userInfo.id
					})
					.then(res => {
						if (res.code === 200) {
							this.userInfo.is_follow = false;
						}
					});
			},
			tabChange(e) {
				let index = e.index;
				this.current = index;
				if (index === 1) {
					this.getPostList();
				}
				if (index === 2) {
					this.getTopicList();
				}
			},
			getPostList() {
				this.loadStatus = 'loading';
				this.$H
					.get('post/list', {
						page: this.page,
						user_id: this.userId
					})
					.then(res => {
						this.postList = this.postList.concat(res.result.data);
						if (res.result.current_page === res.result.last_page || res.result.last_page === 0) {
							this.loadStatus = 'nomore';
						} else {
							this.loadStatus = 'loadmore';
						}
					});
			},
			getTopicList() {
				this.$H.get('topic/userJoinTopic', {
					uid: this.uid
				}).then(res => {
					this.topicList = res.result.data;
				});
			},
			getUserInfo() {
				this.$H.get('user/userInfoById', {
					user_id: this.userId
				}).then(res => {
					this.userInfo = res.result;

					uni.setNavigationBarTitle({
						title: this.userInfo.username
					});

					this.loading = false;
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
	.f-wrap {
		margin-bottom: 30rpx;
	}

	.head-bg {
		width: 100%;
		height: 500rpx;
	}

	.con-c {
		padding: 30rpx;
		position: relative;
		top: -200rpx;
	}

	.user-info {
		display: flex;
		flex-direction: column;
		align-items: center;
		position: relative;
		height: 500rpx;

		.user-c {
			background-color: #ffffff;
			border-radius: 30rpx;
			padding: 20rpx;
			position: absolute;
			top: 100rpx;
			left: 0;
			right: 0;
			box-shadow: 5rpx 10rpx 20rpx #e6e6e6;
			display: flex;
			flex-direction: column;
			align-items: center;

			.username {
				font-size: 40rpx;
				font-weight: bold;
				margin-top: 70rpx;
			}

			.num-box {
				font-size: 24rpx;
				margin: 20rpx 0;
				text-align: center;

				.txt {
					color: #999;
					margin-left: 5rpx;
				}

				text {
					margin-right: 30rpx;
				}
			}

			.desc {
				font-size: 22rpx;
				color: #999;
			}

			.btn-box {
				display: flex;
				margin-top: 20rpx;
			}
		}
	}

	.tab-box {
		margin-top: 30rpx;
		margin-bottom: 30rpx;
	}

	.info-c {
		display: flex;
		flex-direction: column;
	}

	.info-c>text {
		margin-bottom: 20rpx;
		color: #999;
	}

	.info-c .level-box {
		margin-bottom: 20rpx;
		display: flex;
		align-items: center;
		color: #999;

		.level {
			font-size: 20rpx;
			color: #fff;
			padding: 5rpx 10rpx;
			background-color: $themes-color;
			border-radius: 10rpx;
			margin-right: 10rpx;
		}
	}

	/* 标签 */
	.tag-box {}

	.tag-box .tag {
		padding: 10rpx 20rpx;
		border-radius: 20rpx;
		font-size: 24rpx;
		display: inline-block;
		margin-right: 20rpx;
		margin-bottom: 20rpx;
		background-color: #ffebe5;
	}

	.tag-box .tag:nth-child(2n) {
		background-color: #ecf9f2;
	}

	.tag-box .tag:nth-child(3n) {
		background-color: #fff7e5;
	}

	.tag-box .tag:nth-child(5n) {
		background-color: #b3e0ff;
	}

	.level {
		font-weight: bold;
	}

	.vip-txt {
		background-color: #000000;
		color: #fff;
		font-size: 20rpx;
		border-radius: 100rpx;
		padding: 5rpx 20rpx;
		display: inline-block;
		margin-left: 10rpx;
	}

	.follow-btn {
		color: #fff;
		background-color: $themes-color;
		font-size: 28rpx;
		border-radius: 100rpx;
		padding: 10rpx 30rpx;
		display: flex;
		align-items: center;
		white-space: nowrap;

		.text {
			margin-left: 10rpx;
		}
	}
</style>
