<template>
	<view class="container">
		<q-topic-list :list="topicList" :loadStatus="loadStatus"></q-topic-list>

		<!-- 创建圈子按钮 -->
		<view style="height: 120rpx;"></view>
		<view class="f-fixed">
			<view class="topic-add-btn" @click="jump">
				<u-icon name="plus" color="#fff"></u-icon>
				<text class="text">创建圈子</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				topicList: [],
				loadStatus: "loading",
				page: 1
			};
		},
		onLoad() {
			this.getMyTopic();
		},
		onReachBottom() {
			this.page++;
			this.getMyTopic();
		},
		methods: {
			getMyTopic() {
				this.loadStatus = "loading";
				this.$H.get("topic/myCreateTopic", {
					page: this.page
				}).then(res => {
					this.topicList = this.topicList.concat(res.result.data);
					if (res.result.current_page === res.result.last_page || res.result.last_page === 0) {
						this.loadStatus = "nomore";
					} else {
						this.loadStatus = "loadmore"
					}
				})
			},
			jump() {
				uni.navigateTo({
					url: '/pages/topic/add'
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.topic-add-btn {
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: $themes-color;
		padding: 20rpx;
		border-radius: 100px;
		.text{
			color: #fff;
		}
	}
</style>
