<template>
	<view class="container">
		<view class="title">选择圈子类目</view>
		<view class="class-wrap">
			<view class="class-item" @click="chooseClass(item.id,item.name)" v-for="(item, index) in classList" :key="index">{{ item.name }}</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			classList: []
		};
	},
	created() {
		this.getCatList();
	},
	methods: {
		getCatList() {
			this.$H.get('topic/classList').then(res => {
				this.classList = res.result;
			});
		},
		chooseClass(id,name){
			let pages = getCurrentPages(); //获取所有页面栈实例列表
			let nowPage = pages[pages.length - 1]; //当前页页面实例
			let prevPage = pages[pages.length - 2]; //上一页页面实例
			prevPage.$vm.form.class_id = id;
			prevPage.$vm.cateName = name;
			uni.navigateBack();
		}
	}
};
</script>

<style lang="scss" scoped>
.title {
	margin-bottom: 30rpx;
}
.class-wrap {
	display: grid;
	grid-template-columns: repeat(2,1fr);
	grid-gap: 20rpx;
	.class-item {
		border: 1px solid #999;
		padding: 20rpx;
		font-size: 24rpx;
		color: #999;
		text-align: center;
		border-radius: 10rpx;

		&:active{
			background-color: #333;
			color: #fff;
		}
	}
}
</style>
