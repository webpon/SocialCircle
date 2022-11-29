<template>
	<view>
		<view class="head">
			<block v-if="hasLogin">
				<view class="userinfo" @click="toUcenter">
					<u-avatar :customStyle="{marginRight:'20rpx'}" :src="userInfo.avatar"></u-avatar>
					<view class="username">
						<text>{{ userInfo.username }}</text>
						<view class="sub-txt">
							<text>等级：{{ userInfo.level }}</text>
							<view @click.stop="jumpVip" v-if="userInfo.is_vip" url="vip" class="vip-tips">vip会员</view>
						</view>
					</view>
					<view class="arrow-right">
						<u-icon name="arrow-right"></u-icon>
					</view>
				</view>
			</block>
			<block v-else>
				<view class="btn-login">
					<q-button @click="toLogin" plain>登录</q-button>
				</view>
			</block>
			<u-grid :col="4" :border="false" style="margin: 20rpx 0;" @click="toNav">
				<u-grid-item name="/pages/my/fans">
					<text>{{ userInfo.fans || 0 }}</text>
					<view class="grid-text">粉丝</view>
				</u-grid-item>
				<u-grid-item name="/pages/my/follow">
					<text>{{ userInfo.follow || 0 }}</text>
					<view class="grid-text">关注</view>
				</u-grid-item>
				<u-grid-item name="/pages/my/post">
					<text>{{ userInfo.post_num || 0 }}</text>
					<view class="grid-text">帖子</view>
				</u-grid-item>
				<u-grid-item>
					<text>{{ userInfo.integral || 0 }}</text>
					<view class="grid-text">积分</view>
				</u-grid-item>
			</u-grid>
		</view>
		<!-- 社区服务 -->
		<view class="block-wrap">
			<view class="block-title">社区服务</view>
			<u-grid :col="4" :border="false" style="margin: 20rpx 0;" @click="toNav">
				<u-grid-item name="/pages/my/topic">
					<image class="gn-icon" src="/static/m_2.png"></image>
					<view class="grid-text">我的圈子</view>
				</u-grid-item>

				<u-grid-item name="/pages/my/post">
					<image class="gn-icon" src="/static/m_3.png"></image>
					<view class="grid-text">我的帖子</view>
				</u-grid-item>

				<u-grid-item name="/pages/my/thumb">
					<image class="gn-icon" src="/static/m_4.png"></image>
					<view class="grid-text">点赞的帖子</view>
				</u-grid-item>
			</u-grid>
		</view>
		<!-- 帮助 -->
		<view class="block-wrap">
			<view class="block-title">帮助</view>
			<u-grid :col="4" :border="false" style="margin: 20rpx 0;" @click="toNav">
				<u-grid-item>
					<button open-type="contact" class="u-reset-button">
						<image class="gn-icon" style="margin-bottom: unset;" src="/static/m_8.png"></image>
						<view class="grid-text">帮助与反馈</view>
					</button>
				</u-grid-item>
			</u-grid>
		</view>
		<!-- tabbar -->
		<q-tabbar :active="4" :count="msgCount"></q-tabbar>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			};
		},
		computed: {
			hasLogin() {
				return this.$store.state.hasLogin;
			},
			timestamp() {
				return Date.parse(new Date()) / 1000;
			},
			platform() {
				return this.$u.os();
			},
			userInfo() {
				return this.$store.state.userInfo;
			},
			msgCount() {
				return this.$store.state.messegeNum;
			}
		},
		onShow() {
			if (this.userInfo.id) {
				this.getMsgNum();
				this.getUserInfo();
			}
		},
		onLoad() {},
		methods: {
			getMsgNum() {
				this.$H.post('message/num').then(res => {
					this.$store.state.messegeNum = [0, 0, 0, res.result.all_count, 0];
				});
			},
			jumpVip() {
				uni.navigateTo({
					url: 'vip'
				});
			},
			toLogin() {
				uni.navigateTo({
					url: '/pages/user/login'
				});
			},
			getUserInfo() {
				this.$H.get('user/currentUserInfo').then(res => {
					this.$store.commit('login', res.result);
				});
			},
			toUcenter() {
				uni.navigateTo({
					url: '/pages/user/edit-info/edit'
				});
			},
			toNav(url) {
				uni.navigateTo({
					url: url
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
	.head {
		padding: 20rpx;
		background-color: #fff;

		.sub-txt {
			font-size: 24rpx;
			color: #616161;

			.vip-tips {
				display: inline-block;
				background-image: linear-gradient(to right, #f4e4cb, #e9caad, #dfb492);
				color: #fff;
				padding: 0rpx 10rpx;
				border-radius: 100rpx;
				margin-left: 10rpx;
			}
		}

		margin-bottom: 20rpx;
	}

	.userinfo {
		display: flex;
		align-items: center;
		padding: 20rpx;

		.arrow-right {
			margin-left: auto;
		}
	}

	.userinfo .username {
		display: flex;
		flex-direction: column;
	}

	.grid-text {
		color: #999;
		font-size: 12px;
		margin-bottom: 20rpx;
	}


	.btn-login {
		margin: 40rpx 0;
	}

	.gn-icon {
		width: 60rpx;
		height: 60rpx;
		margin-bottom: 20rpx;
	}

	/*服务按钮*/
	.btn-wrap {
		display: flex;
		margin-top: 30rpx;
	}

	.btn-wrap .btn-contact {
		background-color: #fff;
		margin-left: 15rpx;
		margin-right: 15rpx;
		padding: 20rpx;
		line-height: unset;
		font-size: 12px;
		color: #999;
	}

	.btn-wrap .btn-contact:active {
		background-color: #f5f5f5;
	}

	.btn-wrap .btn-contact .txt {
		margin-top: 20rpx;
	}

	.btn-wrap .btn-contact::after {
		border: unset;
		position: unset;
	}

	.icon-size {
		font-size: 50rpx;
	}

	.block-wrap {
		background-color: #fff;
		border-radius: 20rpx;
		margin: 20rpx;
		overflow: hidden;

		.block-title {
			background-color: #fff;
			padding: 20rpx;
		}
	}

	// 开通会员
	.vip-wrap {
		display: flex;
		align-items: center;
		margin: 30rpx;
		background-image: linear-gradient(to right, #f6f2e7, #f5ecdd, #f3e4d1);
		border: 1px solid #d6c6b5;
		padding: 20rpx;
		border-radius: 20rpx;
		color: #8a5d39;

		.left {
			.title {
				font-weight: bold;
			}

			.sub {
				font-size: 24rpx;
			}
		}

		.right {
			margin-left: auto;

			.kt-btn {
				color: #fff;
				background-color: #384048;
				border-radius: 100rpx;
				padding: 10rpx 30rpx;
				font-size: 28rpx;
			}
		}
	}
</style>
