<template>
	<view>
		<q-field v-model="item.name" v-for="(item, index) in calssList" :key="index" :placeholder="'选项' + (index + 1)"
			right-icon="close-circle-fill" @onRightIcon="onRightIcon(item,index)"></q-field>
		<view class="option-add" @click="optionAdd">
			<u-icon name="plus" color="#333"></u-icon>
			<text class="txt">添加分区</text>
		</view>
		<q-button @click="saveForm">保存</q-button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				topicId: '',
				calssList: []
			};
		},
		onLoad(options) {
			this.topicId = options.id;
			this.getTopicClass();
		},
		methods: {
			saveForm() {
				if (this.calssList.length == 0) return;
				
				for (var i = 0; i < this.calssList.length; i++) {
					if(!this.calssList[i].name){
						uni.showToast({
							title: "请填写板块名称",
							icon: "none"
						})
						
						return;
					}
				}
				
				uni.showLoading({
					title: '保存中'
				})

				this.$H.post('topic/saveClass', {
					class_list: this.calssList
				}).then(res => {
					if (res.code == 200) {
						uni.showToast({
							title: "保存成功",
							icon: "none"
						})
					}

					uni.hideLoading();
				});
			},
			getTopicClass() {
				this.$H.get('topic/postClassList', {
					topic_id: this.topicId
				}).then(res => {
					this.calssList = this.calssList.concat(res.result);
				});
			},
			onRightIcon(e, index) {
				let that = this;
				if (e.id) {
					uni.showModal({
						title: '提示',
						content: '确认删除？',
						success: function(res) {
							if (res.confirm) {
								that.$H.get('topic/classDel', {
									id: e.id
								}).then(res => {
									if (res.code == 200) {
										uni.showToast({
											title: "删除成功",
											icon: "none"
										})

										that.calssList.splice(index, 1);
									}
								});
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					});
				} else {
					this.calssList.splice(index, 1);
				}
			},
			optionAdd() {
				this.calssList.push({
					topic_id: this.topicId,
					name: ''
				});
			},
		}
	}
</script>

<style lang="scss" scoped>
	.option-add {
		display: flex;
		align-items: center;
		justify-content: center;
		border: 1px solid #333;
		background-color: #f5f5f5;
		margin: 30rpx;
		padding: 20rpx;
		border-radius: 20rpx;

		.txt {
			margin-left: 10rpx;
		}
	}
</style>
