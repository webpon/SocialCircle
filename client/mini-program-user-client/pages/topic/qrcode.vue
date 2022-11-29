<template>
	<view>
		<view class="share-wrap">
			<view class="desc-text">邀请您加入圈子</view>
			<view class="school-name">{{topicInfo.name}}</view>
			<image class="qrcode-img" :src="qrCode"></image>
			<view class="tips-txt">微信扫一扫</view>
			<view class="into-text">云喵圈子-年轻人同好兴趣社区</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				topicId: '',
				qrCode: '',
				topicInfo: {}
			};
		},
		onLoad(options) {
			this.topicId = options.id;
			this.getTopicInfo();
			this.getQrCode();
		},
		methods: {
			getTopicInfo() {
				this.$H.get("topic/detail", {
					id: this.topicId
				}).then(res => {
					this.topicInfo = res.result;
				})
			},
			getQrCode() {
				this.$H.get("topic/qrCode", {
					topic_id: this.topicId
				}).then(res => {
					if (res.code == 200) {
						this.qrCode = res.result.url;
					}
				})
			}
		}
	}
</script>

<style>
	page {
		background-color: #f5f5f5;
	}
</style>
<style lang="scss" scoped>
	.desc-text{
		color: #999;
		margin-bottom: 30rpx;
	}
	.share-wrap {
		margin: 30rpx;
		border-radius: 30rpx;
		background-color: #fff;
		padding: 50rpx 30rpx;
		display: flex;
		flex-direction: column;
		align-items: center;

		.school-name {
			font-size: 40rpx;
			font-weight: bold;
			margin-bottom: 50rpx;
		}

		.qrcode-img {
			width: 400rpx;
			height: 400rpx;
			display: inline-block;
			margin-bottom: 30rpx;
		}

		.tips-txt {
			color: #999;
			margin-bottom: 60rpx;
			font-size: 28rpx;
		}

		.into-text {
			color: #616161;
			line-height: 1.8;
		}
	}
</style>
