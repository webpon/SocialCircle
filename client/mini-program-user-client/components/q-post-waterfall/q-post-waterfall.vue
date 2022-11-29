<template>
	<view>
		<view class="waterfall-wrap">
			<view>
				<view @click="jump(item)" v-for="(item, index) in leftData" :key="item.id" class="post-item">
					<u-lazy-load v-if="item.type == 1" loading-img="/static/img-load.png" threshold="100" border-radius="10" :image="item.media[0]" :index="item.id"></u-lazy-load>
					<view v-if="item.type == 2" class="video-wrap">
						<image :lazy-load="true" :src="item.media[0] + '?x-oss-process=video/snapshot,t_0,f_jpg'" mode="widthFix"></image>
						<image class="pay-icon" :lazy-load="true" src="../../static/images/play.png"></image>
					</view>
					<view class="post-title">{{ item.content.substring(0, 20) }}</view>
					<view class="footer">
						<image class="avatar" :lazy-load="true" :src="item.userInfo.avatar" mode="aspectFill"></image>
						<text class="username">{{ item.userInfo.username.substring(0, 10) }}</text>
						<text class="thumb-num">{{ item.thumb_num }}</text>
						<u-icon class="thumb-icon" name="heart" :size="36"></u-icon>
					</view>
				</view>
			</view>
			<view>
				<view @click="jump(item)" v-for="(item, index) in rightData" :key="item.id" class="post-item">
					<u-lazy-load v-if="item.type == 1" loading-img="/static/img-load.png" threshold="100" border-radius="10" :image="item.media[0]" :index="item.id"></u-lazy-load>
					<view v-if="item.type == 2" class="video-wrap">
						<image :lazy-load="true" :src="item.media[0] + '?x-oss-process=video/snapshot,t_0,f_jpg'" mode="widthFix"></image>
						<image class="pay-icon" :lazy-load="true" src="../../static/images/play.png"></image>
					</view>
					<view class="post-title">{{ item.content.substring(0, 20) }}</view>
					<view class="footer">
						<image class="avatar" :lazy-load="true" :src="item.userInfo.avatar" mode="aspectFill"></image>
						<text class="username">{{ item.userInfo.username.substring(0, 10) }}</text>
						<text class="thumb-num">{{ item.thumb_num }}</text>
						<u-icon class="thumb-icon" name="heart" :size="36"></u-icon>
					</view>
				</view>
			</view>
		</view>
		<!-- 加载状态 -->
		<view style="margin: 30rpx 0;"><u-loadmore :status="loadStatus" /></view>
	</view>
</template>

<script>
export default {
	name: 'q-post-waterfall',
	props: {
		list: {
			type: Array,
			default: []
		},
		loadStatus: {
			type: String
		}
	},
	data() {
		return {
			leftData: [],
			rightData: []
		};
	},
	watch: {
		list(value) {
			this.handelData();
		}
	},
	methods: {
		handelData() {
			let that = this;
			this.list.forEach((item, index) => {
				let i = index + 1;
				if (i % 2 == 0) {
					that.rightData.push(item);
				} else {
					that.leftData.push(item);
				}
			});
		},
		// 清空数据
		clear() {
			this.leftData = [];
			this.rightData = [];
		},
		jump(e) {
			let url;

			// 图文
			if (e.type == 1 || e.type == 4) {
				url = '/pages/post/detail?id=' + e.id;
			}

			//视频
			if (e.type == 2) {
				url = '/pages/post/video-detail?id=' + e.id;
			}

			uni.navigateTo({
				url: url
			});
		}
	}
};
</script>

<style lang="scss" scoped>
.waterfall-wrap {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	grid-gap: 10rpx;
	padding: 30rpx;
}
.post-item {
	margin-bottom: 50rpx;
	.cover-img {
		width: 100%;
		max-height: 600rpx;
		border-radius: 10rpx;
	}

	.video-wrap {
		position: relative;
		image {
			width: 100%;
			max-height: 600rpx;
			border-radius: 10rpx;
		}
		.pay-icon {
			position: absolute;
			right: 30rpx;
			top: 30rpx;
			width: 50rpx;
			height: 50rpx;
		}
	}
	.post-title {
		font-size: 28rpx;
		margin-bottom: 20rpx;
	}

	.footer {
		display: flex;
		align-items: center;
		font-size: 20rpx;
		color: #999;
		.avatar {
			width: 30rpx;
			height: 30rpx;
			border-radius: 50%;
			margin-right: 10rpx;
		}
		.thumb-num {
			margin-left: auto;
			margin-right: 10rpx;
		}
	}
}
</style>
