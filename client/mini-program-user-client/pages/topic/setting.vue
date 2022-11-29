<template>
	<view class="container">
		<view class="cell-item" @click="jumpMenu('/pages/topic/user-list?id=' + topicId)">
			<view class="left">圈子成员</view>
			<view class="right">
				<u-icon class="icon" name="arrow-right" color="#616161" :size="20"></u-icon>
			</view>
		</view>
		<block v-if="topicInfo.user_id == userInfo.id || topicInfo.is_admin">
			<view class="cell-item" @click="jumpMenu('/pages/topic/info-edit?id=' + topicId)">
				<view class="left">圈子信息</view>
				<view class="right">
					<u-icon class="icon" name="arrow-right" color="#616161" :size="20"></u-icon>
				</view>
			</view>
			<view class="cell-item" @click="jumpMenu('/pages/topic/class-edit?id=' + topicId)">
				<view class="left">板块分区</view>
				<view class="right">
					<u-icon class="icon" name="arrow-right" color="#616161" :size="20"></u-icon>
				</view>
			</view>
		</block>
		<view v-if="topicInfo.user_id == userInfo.id" @click="delTopicModel = true" class="btn-quit">解散圈子</view>
		<view v-else class="btn-quit" @click="outTopic">退出圈子</view>

		<!-- 解散圈子弹窗 -->
		<u-modal :show="delTopicModel" :show-cancel-button="true" :closeOnClickOverlay="true"
			@cancel="delTopicModel = false" confirm-color="red" confirm-text="确认" @close="delTopicModel = false"
			:content="'解散【' + topicInfo.name + '】后，将不可恢复，是否确认解散？'" @confirm="topicDel"></u-modal>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				delTopicModel: false,
				topicId: '',
				topicInfo: {}
			};
		},
		computed: {
			userInfo() {
				return this.$store.state.userInfo;
			}
		},
		onLoad(options) {
			this.topicId = options.id;
			this.getTopicInfo();
		},
		methods: {
			outTopic() {
				let that = this;
				uni.showModal({
					title: '提示',
					content: '确认退出圈子',
					success: function(res) {
						if (res.confirm) {
							that.$H
								.post('topic/userQuit', {
									id: that.topicId
								})
								.then(res => {
									if (res.code === 200) {
										uni.switchTab({
											url: '/pages/index/index'
										});
									}
								});
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			// 解散圈子
			topicDel() {
				uni.showLoading({
					mask: true,
					title: '操作中'
				});
				this.$H.post('topic/topicDel', {
					id: this.topicId
				}).then(res => {
					if (res.code == 200) {
						this.$u.toast('该圈子已成功解散');

						setTimeout(function() {
							uni.switchTab({
								url: '/pages/index/index'
							});
						}, 1500);
					}
					uni.hideLoading();
				});
			},
			getTopicInfo() {
				this.$H.get('topic/detail', {
					id: this.topicId
				}).then(res => {
					this.topicInfo = res.result;
				});
			},
			jumpMenu(url) {
				this.showMenu = false;
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
	.cell-item {
		display: flex;
		align-items: center;
		line-height: 1;
		background-color: #fff;
		padding: 30rpx;
		border-radius: 20rpx;
		margin-bottom: 20rpx;

		&:last-child {
			border-bottom: 0;
		}

		.right {
			margin-left: auto;

			.icon {
				margin-left: 10rpx;
			}
		}
	}

	.btn-quit {
		background-color: #fff;
		border-radius: 20rpx;
		text-align: center;
		font-size: 28rpx;
		padding: 20rpx;
		color: red;
	}
</style>
