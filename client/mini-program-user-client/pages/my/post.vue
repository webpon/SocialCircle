<template>
	<view>
		<q-post-list :list="postList" :loadStatus="loadStatus"></q-post-list>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				postList: [],
				loadStatus: "loading",
				page: 1
			};
		},
		onLoad() {
			this.getPostList();
		},
		onReachBottom() {
			this.page++;
			this.getPostList();

		},
		methods: {
			getPostList() {
				this.loadStatus = "loading";
				this.$H.get('post/releaseList', {
					page: this.page
				}).then(res => {
					this.postList = this.postList.concat(res.result.data);
					if (res.result.current_page === res.result.last_page || res.result.last_page === 0) {
						this.loadStatus = "nomore";
					} else {
						this.loadStatus = "loadmore"
					}
				})
			}
		}
	}
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>
