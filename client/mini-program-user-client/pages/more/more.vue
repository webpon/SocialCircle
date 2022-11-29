<template>
	<view>
		<!-- navbar -->
		<u-navbar :is-back="false" :border-bottom="false" :placeholder="true">
			<view slot="left">
				<navigator url="/pages/topic/add" class="topic-add-btn">
					<u-icon name="plus" color="#fff"></u-icon>
					<text class="text">创建云圈</text>
				</navigator>
			</view>
		</u-navbar>
		<!-- 搜索 -->
		<view class="serach-box">
			<u-search @click="jumpSearch" :disabled="true" placeholder="查找动态|圈子|用户" :show-action="false"></u-search>
		</view>
		<!-- 轮播图 -->
		<view v-if="swiperList.length > 0" class="swiper-box">
			<u-swiper @click="onSwiper" :list="swiperList" keyName="img" border-radius="20" mode="none"></u-swiper>
		</view>
		<!-- 我的圈子 -->
		<block v-if="joinTopicList.length > 0">
			<view class="block-title">
				<view class="left">我的圈子</view>
				<view class="right">
					<text>加入和创建的圈子</text>
				</view>
			</view>
			<scroll-view scroll-x>
				<view class="hot-topic">
					<navigator hover-class="none" :url="'/pages/topic/detail?id=' + item.id" class="topic-item"
						v-for="(item, index) in joinTopicList" :key="index">
						<view class="cover-box">
							<image :src="item.cover_image" class="cover-img" mode="aspectFill"></image>
						</view>
						<view class="footer">
							<text class="name">{{ item.name }}</text>
							<text class="user-num">{{ item.user_num | formatNum }}圈友</text>
						</view>
					</navigator>
				</view>
			</scroll-view>
		</block>
		<!-- 热门圈子 -->
		<view class="block-title">
			<view class="left">热门圈子</view>
			<navigator url="/pages/topic/class-list" hover-class="none" class="right">
				<text>更多圈子</text>
				<u-icon name="arrow-right" size="30rpx" color="#000"></u-icon>
			</navigator>
		</view>
		<scroll-view scroll-x>
			<view class="hot-topic">
				<navigator hover-class="none" :url="'/pages/topic/detail?id=' + item.id" class="topic-item"
					v-for="(item, index) in topicHotList" :key="index">
					<view class="cover-box">
						<image :src="item.cover_image" class="cover-img" mode="aspectFill"></image>
					</view>
					<view class="footer">
						<text class="name">{{ item.name }}</text>
						<text class="user-num">{{ item.user_num | formatNum }}圈友</text>
					</view>
				</navigator>
			</view>
		</scroll-view>
		<!-- 大家在聊 -->
		<view class="discuss-wrap">
			<view class="title">大家在聊</view>
			<view class="discuss-list">
				<navigator :url="'/pages/discuss/detail?title='+item.title" class="discuss-item u-line-1"
					v-for="(item,index) in discussList" :key="index">#{{item.title}}#
				</navigator>
			</view>
		</view>
		<!-- tabs -->
		<u-tabs :list="classList" keyName="name" lineColor="#000" :current="current" @change="tabChange"></u-tabs>
		<!-- 圈子列表 -->
		<navigator class="topic-wrap" hover-class="none" :url="'/pages/topic/detail?id=' + item.id"
			v-for="(item, index) in topicList" :key="index">
			<view class="info-wrap">
				<image class="cover-img" mode="aspectFill" :src="item.cover_image"></image>
				<view class="right">
					<view class="name">{{ item.name }}</view>
					<view class="count-wrap">
						<text>{{ item.user_num }}人加入</text>
						<text>{{ item.post_num }}条动态</text>
					</view>
				</view>
			</view>
			<view class="post-img-wrap" v-if="item.img_list.length > 0">
				<image mode="aspectFill" v-for="(item2, index2) in item.img_list" :key="index2" :src="item2"></image>
			</view>
		</navigator>
		<!-- 加载状态 -->
		<block v-if="topicList.length === 0 && loadStatus == 'nomore'">
			<u-empty text="暂无内容" mode="favor" icon="/static/empty.png"></u-empty>
		</block>
		<block v-else>
			<view style="margin: 30rpx 0;">
				<u-loadmore :status="loadStatus" />
			</view>
		</block>
		<!-- tabbar -->
		<q-tabbar :active="1" :count="msgCount"></q-tabbar>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				classList: [{
					id: '',
					name: '最新'
				}],
				classId: '',
				topicList: [],
				loadStatus: 'loadmore',
				current: 0,
				swiperList: [],
				topicHotList: [],
				joinTopicList: [],
				discussList: []
			};
		},
		computed: {
			msgCount() {
				return this.$store.state.messegeNum;
			}
		},
		filters: {
			formatNum(val) {
				let numStr = val;
				let num;
				if (val > 1000) {
					num = (val / 1000).toFixed(1);

					numStr = num + 'k';
				}

				if (val > 10000) {
					num = (val / 10000).toFixed(1);
					numStr = num + 'w';
				}

				return numStr;
			}
		},
		onLoad() {
			this.getClassList();
			this.getLinkList();
			this.getHotTopic();
			this.getTopicList();
			this.getDiscussList();
		},
		onShow() {
			this.getMsgNum();
		},
		onReachBottom() {
			this.page++;
			this.getTopicList();
		},
		onPullDownRefresh() {
			this.getLinkList();
			this.getHotTopic();
			this.getClassList();
			this.page = 1;
			this.topicList = [];
			this.getTopicList();
			this.getDiscussList();
			uni.stopPullDownRefresh();
		},
		methods: {
			jumpSearch() {
				uni.navigateTo({
					url: '/pages/search/search'
				});
			},
			getHotTopic() {
				this.$H.get('topic/hot', {
					count: 10
				}).then(res => {
					this.topicHotList = res.result;
				});
			},
			jumpTopic(id) {
				uni.navigateTo({
					url: '/pages/topic/detail?id=' + id
				});
			},
			previewImage(url, urls) {
				uni.previewImage({
					current: url, // 当前显示图片的http链接
					urls: urls // 需要预览的图片http链接列表
				});
			},
			// 获取圈子列表
			getTopicList() {
				this.loadStatus = 'loading';
				this.$H
					.get('topic/classTopicAreImg', {
						class_id: this.classId,
						page: this.page
					})
					.then(res => {
						this.topicList = this.topicList.concat(res.result.data);
						if (res.result.current_page === res.result.last_page || res.result.last_page === 0) {
							this.loadStatus = 'nomore';
						} else {
							this.loadStatus = 'loadmore';
						}
					});
			},
			// 处理点击轮播图跳转
			onSwiper(index) {
				let link = this.swiperList[index];

				//应用内页面
				if (link.open == 1) {
					uni.navigateTo({
						url: link.url
					});
				}

				//第三方小程序
				if (link.open == 2) {
					uni.navigateToMiniProgram({
						appId: link.appid,
						path: link.url
					})
				}

				//H5页面
				if (link.open == 3) {
					uni.navigateTo({
						url: '/pages/webview/webview?src=' + url
					});
				}
			},
			// 获取轮播图
			getLinkList() {
				this.$H
					.get('link/list', {
						type: 1
					})
					.then(res => {
						this.swiperList = res.result.data;
					});
			},
			getMsgNum() {
				this.$H.post('message/num').then(res => {
					this.$store.state.messegeNum = [0, 0, 0, res.result.all_count, 0];
				});
			},
			getClassList() {
				let that = this;
				this.$H.get('topic/classList').then(res => {
					let classList = [{
						id: '',
						name: '最新'
					}];

					this.classList = classList.concat(res.result);
				});
			},
			tabChange(e) {
				let index = e.index;
				this.page = 1;
				this.current = index;
				this.classId = this.classList[index].id;
				this.topicList = [];
				this.getTopicList();
			},
			getDiscussList() {
				this.$H.get('discuss/random').then(res => {
					this.discussList = res.result;
				});
			}
		}
	};
</script>
<style lang="scss" scoped>
	.discuss-wrap {
		margin: 30rpx;

		.title {
			margin-bottom: 20rpx;
		}

		.discuss-list {
			font-size: 28rpx;
			display: grid;
			grid-template-columns: repeat(2, 1fr);
			grid-gap: 20rpx;

			.discuss-item {
				font-weight: bold;
			}
		}

	}

	.topic-add-btn {
		display: flex;
		align-items: center;
		font-size: 24rpx;
		background-color: $themes-color;
		border-radius: 100rpx;
		padding: 10rpx 30rpx;
		margin-left: 20rpx;
		color: #fff;

		.text {
			margin-left: 10rpx;
		}
	}

	.serach-box {
		margin: 0 20rpx;
	}

	.swiper-box {
		padding: 20rpx;
	}

	// 热门圈子
	.hot-topic {
		padding: 20rpx;
		display: flex;

		.topic-item {
			box-shadow: 0 0 10rpx #eee;
			margin: 0 10rpx;
			border-radius: 10rpx;
			width: 200rpx;
			flex-shrink: 0;

			.cover-box {
				position: relative;

				.cover-img {
					width: 100%;
					height: 200rpx;
					border-radius: 10rpx 10rpx 0 0;
					display: block;
				}
			}

			.footer {
				font-size: 24rpx;
				padding: 20rpx;

				.name {
					display: inline-block;
					width: 100%;
					white-space: nowrap;
					overflow: hidden;
					text-overflow: ellipsis;
					font-weight: bold;
				}

				.user-num {
					color: #999;
					white-space: nowrap;
				}
			}
		}
	}

	// 块标题
	.block-title {
		font-weight: bold;
		padding: 20rpx;
		color: #000;
		display: flex;
		font-size: 28rpx;

		.right {
			margin-left: auto;
			color: #000;
			font-size: 24rpx;
			display: flex;
			align-items: center;
		}
	}

	// 圈子列表
	.topic-wrap {
		box-shadow: 0 0 10rpx #eee;
		margin: 30rpx;
		padding: 20rpx;
		border-radius: 20rpx;

		.info-wrap {
			display: flex;

			.cover-img {
				width: 100rpx;
				height: 100rpx;
				border-radius: 50%;
				margin-right: 20rpx;
			}

			.right {
				.name {
					font-weight: bold;
				}

				.count-wrap {
					font-size: 24rpx;
					color: #616161;

					text {
						margin-right: 10rpx;
					}
				}
			}
		}

		.post-img-wrap {
			display: grid;
			grid-template-columns: repeat(3, 1fr);
			grid-gap: 10rpx;
			margin-top: 20rpx;

			image {
				width: 100%;
				height: 200rpx;
				border-radius: 10rpx;
			}
		}
	}
</style>
