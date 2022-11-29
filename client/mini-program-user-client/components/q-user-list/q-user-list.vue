<template>
	<view>
		<block v-for="(item, index) in userList" :key="index">
			<navigator :url="'/pages/user/home?user_id=' + item.id" class="member-item">
				<u-avatar class="avatar" :customStyle="{marginRight: '20rpx'}" :src="item.avatar"></u-avatar>
				<view class="user">
					<text class="name">{{ item.username }}</text>
					<text v-if="item.gender == '男'" class="iconfont icon-nan"></text>
					<text v-if="item.gender == '女'" class="iconfont icon-nv"></text>
				</view>
				<view @click.stop="follow(index, item.id)" class="btn-item" v-if="item.has_follow === 0">关注</view>
				<view @click.stop="cancelFollow(index, item.id)" class="btn-item none-btn-item" v-if="item.has_follow === 1">互相关注</view>
				<view @click.stop="cancelFollow(index, item.id)" class="btn-item none-btn-item" v-if="item.has_follow === 2">已关注</view>
			</navigator>
		</block>
		<!-- 加载状态 -->
		<block v-if="loadStatus != 'none'">
			<block v-if="list.length === 0 && loadStatus == 'nomore'"><u-empty margin-top="100" text="暂无用户" mode="favor"></u-empty></block>
			<block v-else><u-loadmore margin-bottom="50" margin-top="50" :status="loadStatus" /></block>
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
			userList: []
		};
	},
	watch: {
		list() {
			this.userList = this.list;
		}
	},
	methods: {
		follow(index, uid) {
			this.$H
				.post('user/addFollow', {
					id: uid
				})
				.then(res => {
					if (res.code == 200) {
						this.userList[index].has_follow = 1;
					}
				});
		},
		cancelFollow(index, uid) {
			this.$H
				.post('user/cancelFollow', {
					id: uid
				})
				.then(res => {
					if (res.code === 200) {
						this.userList[index].has_follow = 0;
					}
				});
		}
	}
};
</script>

<style lang="scss" scoped>
.member-item {
	display: flex;
	align-items: center;
	padding: 20rpx;
	border-bottom: 1px solid #f5f5f5;
	background-color: #ffffff;
	&:last-child{
		border-bottom: 0;
	}
	
}

.member-item .icon-nv {
	color: #ff4d94;
}

.member-item .icon-nan {
	color: #0091ff;
}

.member-item .user .name {
	margin-right: 20rpx;
}

.member-item .user .iconfont {
	font-size: 12px;
}
.btn-item{
	margin-left: auto;
	background-color: $themes-color;
	font-size: 28rpx;
	padding: 5rpx 20rpx;
	border-radius: 100px;
	color: #fff;
	white-space: nowrap;
}

.none-btn-item{
	background-color:#eee
}

</style>
