<template>
	<view>
		<q-field label="圈子类目" @click="jumpChooseTopicClass" type="select" :isClick="true" :required="true" :placeholder="cateName"></q-field>
		<q-field label="圈子名称" v-model="form.name" :required="true" placeholder="请填写圈子名称"></q-field>
		<q-field label="圈子介绍" type="textarea" v-model="form.description" :required="true" placeholder="请填写圈子介绍"></q-field>
		<view style="margin: 30rpx;">
			<u-alert-tips :show="true" title="正确选择圈子类目能提高圈子曝光率,认真填写圈子信息能更好吸引圈友加入"></u-alert-tips>
		</view>
		<!-- 提交按钮 -->
		<q-button shape="circle" @click="submit">提交</q-button>
	</view>
</template>

<script>
export default {
	data() {
		return {
			cateName:'请选择圈子类目',
			form: {
				class_id:'',
				name: '',
				description: ''
			}
		};
	},
	onLoad() {},
	methods: {
		submit() {
			if (!this.form.name) {
				this.$u.toast('请填写圈子名称');
				return;
			}

			if (!this.form.description) {
				this.$u.toast('请填写圈子介绍');
				return;
			}

			this.$H.post('topic/topicSave', this.form).then(res => {
				if (res.code == 200) {
					this.$u.toast('成功创建圈子');
					uni.redirectTo({
						url: '/pages/topic/detail?id=' + res.result.id
					});
				}
			});
		},
		jumpChooseTopicClass() {
			uni.navigateTo({
				url: '/pages/topic/shoose-class'
			});
		}
	}
};
</script>

<style>
page {
	background-color: #fff;
}

.upload-wrap {
	padding: 30rpx;
}
</style>
