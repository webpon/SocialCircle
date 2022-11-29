<template>
	<view>
		<block v-for="(item,index) in list" :key="index">
			<navigator :url="'/pages/topic/detail?id='+item.id">
				<view class="topic-item">
					<u-image :customStyle="{marginRight:'20rpx'}" width="120rpx" height="120rpx" radius="10rpx" :src="item.cover_image"></u-image>
					<view class="center">
						<view>{{item.name.substring(0,10)}}</view>
						<view class="desc">{{item.description|replace}}</view>
					</view>
					<view class="count-box">
						<text>{{item.user_num}}圈友</text>
						<text>{{item.post_num}}动态</text>
					</view>
				</view>
			</navigator>
		</block>
		<!-- 加载状态 -->
		<block v-if="loadStatus != 'none'">
			<block v-if="list.length > 0">
				<view style="margin: 30rpx;">
					<u-loadmore :status="loadStatus" />
				</view>
			</block>
			<block v-else>
				<u-empty text="暂无相关圈子" mode="favor" icon="/static/empty.png"></u-empty>
			</block>
		</block>
	</view>
</template>

<script>
	export default {
		props: {
			list: Array,
			loadStatus: String
		},
		data() {
			return {

			};
		},
		filters: {
			replace(str) {
				str = str.replace(/\n/g, '');
				return str.substring(0,40);
			}
		},
	}
</script>

<style lang="scss" scoped>
	.topic-item {
		display: flex;
		align-items: center;
		background-color: #fff;
		padding: 30rpx;
		border-bottom: 1px solid #f5f5f5;
		.center{
			flex: 1;
			.desc{
				font-size: 20rpx;
				color: #999;
			}
		}
		.count-box{
			display: flex;
			flex-direction: column;
			font-size: 20rpx;
			color: #999;
			margin-left: 20rpx;
		}
	}
</style>
