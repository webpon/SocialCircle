<template>
	<view>
		<view class="user-item">
			<image class="avatar" @click="jumpUser(postDetail.user_id)" mode="aspectFill"
				:src="postDetail.userInfo.avatar">
			</image>
			<text class="username">{{ postDetail.userInfo.username }}</text>
			<view class="btn" v-if="postDetail.is_follow" size="mini" @click="cancelFollow">已关注</view>
			<view class="btn" v-else size="mini" @click="follow">
				<u-icon name="plus"></u-icon>
				<text>关注</text>
			</view>
		</view>
		<view class="info-box">
			<!-- 轮播图 -->
			<block v-if="postDetail.type == 1 && postDetail.media.length > 0">
				<u-swiper @click="handelSwiper" :list="postDetail.media" radius="0"
					@change="e => currentNum = e.current" height="800rpx">
					<view slot="indicator" class="indicator-num">
						<text class="indicator-num__text">{{ currentNum + 1 }}/{{ postDetail.media.length }}</text>
					</view>
				</u-swiper>
			</block>
			<view class="post-content-wrap">
				<navigator class="discuss-item" :url="'/pages/discuss/detail?title='+item"
					v-for="(item,index) in postDetail.discuss_list" :key="index">#{{item}}#</navigator>
				<text @longpress="onCopy">{{postDetail.content}}</text>
				<!-- <rich-text @longpress="onCopy" :nodes="postDetail.content"></rich-text> -->
			</view>
			<!-- 视频 -->
			<block v-if="postDetail.type == 2 && postDetail.media.length > 0"><video :controls="false" :autoplay="true"
					:src="postDetail.media[0]"></video></block>
			<!-- 投票 -->
			<view v-if="postDetail.type === 4" class="vote-box">
				<view class="title">{{ postDetail.vote_info.title }}</view>
				<view class="expire-time" v-if="postDetail.vote_info.type === 1">单选</view>
				<view class="expire-time" v-if="postDetail.vote_info.type === 2">多选</view>
				<!-- 是否投票失效 -->
				<view class="expire-box" v-if="isVoteExpire">投票过期了</view>
				<view v-else class="expire-time">截止：{{ postDetail.vote_info.expire_time | date('yyyy年mm月dd日hh时MM分') }}
				</view>
				<view class="vote-item" @click="castVote(index2, postDetail.vote_info.type)"
					:class="{ active: item2.checked }" v-for="(item2, index2) in postDetail.vote_info.options"
					:key="index2">
					<text v-if="postDetail.is_vote_result || isVoteExpire">
						{{ item2.content }}
						<text style="color: #999;margin-left: 20rpx;">({{ item2.ticket_num }}票)</text>
					</text>
					<text v-else="postDetail.is_vote_result">{{ item2.content }}</text>
				</view>
				<u-button v-if="!postDetail.is_vote_result && isVoteExpire === false" @click="voteSubmit">投票</u-button>
			</view>
			<!-- 圈子信息 -->
			<navigator v-if="postDetail.topic_info" class="topic-info"
				:url="'/pages/topic/detail?id=' + postDetail.topic_id">
				<image mode="aspectFill" class="cover" :src="postDetail.topic_info.cover_image"></image>
				<view class="center">
					<view class="desc">{{ postDetail.topic_info.name.substring(0, 15) }}</view>
					<view class="count-txt">{{ postDetail.topic_info.user_num }}人加圈 |
						{{ postDetail.topic_info.post_num }}篇内容
					</view>
				</view>
				<view class="right">进圈子</view>
			</navigator>
			<!--点赞、分享、评论-->
			<view class="p-footer">
				<block v-if="postDetail.is_thumb">
					<view class="p-item" @click="cancelCollection">
						<u-icon name="heart-fill" color="#d81e06" class="icon" size="45rpx"></u-icon>
						<text>{{ postDetail.thumb_num }}</text>
					</view>
				</block>
				<block v-else>
					<view class="p-item" @click="addCollection">
						<u-icon name="heart" class="icon" size="45rpx"></u-icon>
						<text>{{ postDetail.thumb_num }}</text>
					</view>
				</block>
				<view class="p-item" @click="focus = true">
					<u-icon name="chat" class="icon" size="45rpx"></u-icon>
					<text>{{ postDetail.comment_num }}</text>
				</view>
				<button open-type="share" class="u-reset-button p-item">
					<u-icon name="share-square" class="icon" size="45rpx"></u-icon>
					<text>分享</text>
				</button>
			</view>
		</view>
		<view class="comment-box">
			<view class="title">评论（{{ commentList.length }}）</view>
			<view style="margin-bottom: 40rpx;" v-for="(item, index) in commentList" :key="index">
				<view class="comment-item" @longpress="delComment(item, index)">
					<image @click="jumpUser(item.userInfo.id)" class="avatar" :src="item.userInfo.avatar"></image>
					<view class="c-content" @click="onReply(item)">
						<view class="user">
							<text>{{ item.userInfo.username }}</text>
							<block v-if="item.is_thumb">
								<view @click.stop="cancelThumb(item.id, index)" class="thumbs">
									<text class="num">{{ item.thumb_num }}</text>
									<u-icon class="icon" size="45rpx" name="heart-fill" color="#e62e00"></u-icon>
								</view>
							</block>
							<block v-else>
								<view @click.stop="onThumb(item.id, index)" class="thumbs">
									<text class="num">{{ item.thumb_num }}</text>
									<u-icon class="icon" size="45rpx" name="heart-fill" color="#bfbfbf"></u-icon>
								</view>
							</block>
						</view>
						<text class="c-txt">{{ item.content }}</text>
						<text class="time">{{ item.create_time | timeFrom }}</text>
					</view>
				</view>
				<view @longpress="delComment(item, index, index2)" @click="onReply(item2, index, index2)"
					v-if="item.children.length > 0" v-for="(item2, index2) in item.children" :key="item2.id"
					class="sub-comment-item">
					<view class="user">
						<u-avatar @click="jumpUser(item2.userInfo.id)" :customStyle="{marginRight:'10rpx'}"
							class="avatar" :size="40" :src="item2.userInfo.avatar"></u-avatar>
						<view class="u-head">
							<text>{{ item2.userInfo.username }}</text>
							<block v-if="item2.is_thumb">
								<view class="thumbs" @click.stop="cancelThumb(item2.id, index, index2)">
									<text class="num">{{ item2.thumb_num }}</text>
									<u-icon class="icon" size="45rpx" name="heart-fill" color="#e62e00"></u-icon>
								</view>
							</block>
							<block v-else>
								<view class="thumbs" @click.stop="onThumb(item2.id, index, index2)">
									<text class="num">{{ item2.thumb_num }}</text>
									<u-icon class="icon" size="45rpx" name="heart-fill" color="#bfbfbf"></u-icon>
								</view>
							</block>
						</view>
					</view>
					<view class="reply">
						<text>回复</text>
						<navigator :url="'/pages/user/home?user_id=' + item2.to_user.id" hover-class="none"
							class="name">
							{{ item2.to_user.username }}
						</navigator>
						<text style="color: #616161;">：{{ item2.content }}</text>
						<view class="time">{{ item2.create_time | timeFrom }}</view>
					</view>
				</view>
			</view>
			<!-- 加载状态 -->
			<block v-if="commentList.length > 0">
				<view style="margin: 30rpx;">
					<u-loadmore :status="loadStatus" />
				</view>
			</block>
			<block v-else>
				<u-empty text="暂无评论" mode="message" icon="/static/empty.png"></u-empty>
			</block>
		</view>
		<view style="height: 100rpx;"></view>
		<!-- 评论输入框 -->
		<view class="comment-tool">
			<textarea :placeholder="placeholder" @blur="onBlur" :focus="focus" fixed="true" cursor-spacing="10"
				v-model="form.content" auto-height="true" placeholder-class="txt-placeholder"></textarea>
			<u-button type="warning" :customStyle="{width:'100px'}" @click="addComment" :disabled="isSubmitD">发布
			</u-button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				currentNum: 0,
				postId: 0,
				postDetail: {
					comment: [],
					media: [],
					comment_list: {
						data: []
					},
					topic_info: {
						name: ''
					},
					userInfo: {
						avatar: ''
					}
				},
				focus: false,
				isSubmitD: false,
				commentList: [],
				placeholder: '说点什么...',
				form: {
					pid: 0,
					type: 1,
					to_uid: '',
					post_id: '',
					content: ''
				},
				page: 1,
				loadStatus: 'loadmore',
				isVoteExpire: false,
				discussList: []
			};
		},
		computed:{
			userInfo(){
				return this.$store.state.userInfo
			}
		},
		onLoad(options) {
			this.postId = options.id;

			if (options.scene) {
				this.postId = options.scene;
			}

			this.form.post_id = this.postId;
			this.getPostDetail();
			this.getCommentList();

			//#ifdef MP-WEIXIN
			wx.showShareMenu({
				withShareTicket: true,
				menus: ['shareAppMessage', 'shareTimeline']
			});
			//#endif
		},
		onReachBottom() {
			this.page++;
			this.getCommentList();
		},
		onShareAppMessage(res) {
			if (res.from === 'button') {
				// 来自页面内分享按钮
				console.log(res.target);
			}
			let imgURL;
			if (this.postDetail.media.length > 0) {
				imgURL = this.postDetail.media[0];
			}
			return {
				title: this.postDetail.content,
				path: '/pages/post/detail?id=' + this.postId,
				imageUrl: imgURL
			};
		},
		onShareTimeline() {
			let imgURL = (imgURL = this.postDetail.media[0]);
			return {
				title: this.postDetail.content,
				imageUrl: imgURL,
				query: 'id=' + this.postId
			};
		},
		methods: {
			handelSwiper(index) {
				uni.previewImage({
					current: index, // 当前显示图片的http链接
					urls: this.postDetail.media // 需要预览的图片http链接列表
				});
			},
			voteSubmit() {
				let voteDate = [];
				this.postDetail.vote_info.options.forEach(item => {
					if (item.checked) {
						voteDate.push(item.id);
					}
				});

				this.$H
					.post('vote/userVote', {
						id: this.postDetail.vote_info.id,
						vote: voteDate
					})
					.then(res => {
						if (res.code == 200) {
							this.getPostDetail();
						}
					});
			},
			castVote(index, type) {
				if (!this.postDetail.is_vote_result && !this.isVoteExpire) {
					if (type === 1) {
						this.postDetail.vote_info.options.forEach(item => {
							this.$set(item, 'checked', false);
						});
					}
					let checked = this.postDetail.vote_info.options[index].checked;
					if (checked) {
						this.$set(this.postDetail.vote_info.options[index], 'checked', false);
					} else {
						this.$set(this.postDetail.vote_info.options[index], 'checked', true);
					}
				}
			},
			// 删除评论
			delComment(e, index, index2) {
				let that = this;

				if (e.user_id != this.userInfo.id) {
					return;
				}

				uni.showModal({
					title: '提示',
					content: '确定删除该评论？',
					success: function(res) {
						if (res.confirm) {
							that.$H.post('comment/del', {
									id: e.id
								}).then(res2 => {
									if (res2.code == 200) {
										if (index2 || index2 === 0) {
											that.commentList[index].children.splice(index2, 1);
										} else {
											that.commentList.splice(index, 1);
										}
									}
								});
						} else if (res.cancel) {
							// console.log('用户点击取消');
						}
					}
				});
			},
			onCopy() {
				let that = this;
				uni.setClipboardData({
					data: this.postDetail.content,
					success: function() {
						uni.showToast({
							title: '复制成功'
						});
					}
				});
			},
			// 评论点赞
			onThumb(id, index, index2) {
				this.$H
					.post('comment/thumbAdd', {
						id: id
					})
					.then(res => {
						if (res.code == 200) {
							if (index2 || index2 == 0) {
								this.commentList[index].children[index2].is_thumb = true;
								this.commentList[index].children[index2].thumb_num++;
							} else {
								this.commentList[index].is_thumb = true;
								this.commentList[index].thumb_num++;
							}
						}
					});
			},
			// 评论取消点赞
			cancelThumb(id, index, index2) {
				this.$H
					.post('comment/cancelThumb', {
						id: id
					})
					.then(res => {
						if (res.code == 200) {
							if (index2 || index2 == 0) {
								this.commentList[index].children[index2].is_thumb = false;
								this.commentList[index].children[index2].thumb_num--;
							} else {
								this.commentList[index].is_thumb = false;
								this.commentList[index].thumb_num--;
							}
						}
					});
			},
			// 回复评论
			onReply(e) {
				this.placeholder = '回复：' + e.userInfo.username;
				this.focus = true;

				let pid = e.pid;
				if (pid == 0) {
					this.form.pid = e.id;
				} else {
					this.form.pid = e.pid;
				}

				this.form.to_uid = e.userInfo.id;
				this.form.post_id = this.postId;
			},
			// 输入框失去焦点时
			onBlur() {
				this.placeholder = '说点什么...';
				this.focus = false;
				this.form.pid = 0;
			},
			// 获取评论列表
			getCommentList() {
				this.loadStatus = 'loading';
				this.$H
					.get('comment/list', {
						post_id: this.postId,
						page: this.page
					})
					.then(res => {
						if (res.code == 200) {
							this.commentList = this.commentList.concat(res.result.data);

							if (res.result.current_page === res.result.last_page || res.result.last_page === 0) {
								this.loadStatus = 'nomore';
							} else {
								this.loadStatus = 'loadmore';
							}
						}
					});
			},
			jumpUser(userId) {
				uni.navigateTo({
					url: '/pages/user/home?user_id=' + userId
				});
			},
			addComment() {
				this.isSubmitD = true;
				if (this.form.content == '') {
					this.$u.toast('评论不能为空');
					this.isSubmitD = false;
					return;
				}

				uni.showLoading({
					mask: true,
					title: '发布中'
				});

				this.$H.post('comment/addComment', this.form).then(res => {
					if (res.code == 200) {
						this.form.content = '';
						this.$u.toast('评论成功');
						this.page = 1;
						this.commentList = [];
						this.getCommentList();
					}
					this.isSubmitD = false;
					uni.hideLoading();
				});
			},
			getPostDetail() {
				this.$H
					.get('post/detail', {
						id: this.postId
					})
					.then(res => {

						if (res.result.media) {
							res.result.media.forEach((item, index) => {
								res.result.media[index] = item + '?x-oss-process=image/resize,w_500,m_lfit'
							})
						}

						this.postDetail = res.result;


						// 投票帖子
						if (res.result.vote_id > 0) {
							res.result.vote_info.options.forEach(item => {
								this.$set(item, 'checked', false);
							});

							let timestamp = Date.parse(new Date()) / 1000;
							if (res.result.vote_info.expire_time < timestamp) {
								this.isVoteExpire = true;
							}
						}
					});
			},
			cancelCollection() {
				this.$H
					.post('post/thumbCancel', {
						id: this.postId
					})
					.then(res => {
						if (res.code === 200) {
							this.postDetail.is_thumb = false;
							this.postDetail.thumb_num--;
						}
					});
			},
			addCollection() {
				this.$H
					.post('post/thumbAdd', {
						id: this.postId,
						user_id: this.postDetail.user_id
					})
					.then(res => {
						if (res.code === 200) {
							this.postDetail.is_thumb = true;
							this.postDetail.thumb_num++;
						}
					});
			},
			follow() {
				this.$H
					.post('user/addFollow', {
						user_id: this.postDetail.user_id
					})
					.then(res => {
						if (res.code == 200) {
							this.postDetail.is_follow = true;
						}
					});
			},
			cancelFollow() {
				this.$H
					.post('user/cancelFollow', {
						user_id: this.postDetail.user_id
					})
					.then(res => {
						if (res.code == 200) {
							this.postDetail.is_follow = false;
						}
					});
			},
			previewImage(e) {
				uni.previewImage({
					current: e.currentTarget.dataset.current, // 当前显示图片的http链接
					urls: e.currentTarget.dataset.image // 需要预览的图片http链接列表
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
	.post-title {
		margin: 20rpx 0;
	}

	.icon text {
		font-size: 12px;
		color: #999;
		margin-right: 20rpx;
	}

	/*关注*/

	.user-item {
		padding: 30rpx;
		display: flex;

		.avatar {
			width: 60rpx;
			height: 60rpx;
			margin-right: 10rpx;
			border-radius: 50%;
		}

		.btn {
			border: 1px solid #F5F5F5;
			border-radius: 50rpx;
			font-size: 20rpx;
			margin-left: auto;
			margin-right: 30rpx;
			display: flex;
			align-items: center;
			line-height: 1;
			padding: 10rpx 20rpx;
		}
	}

	/*底部操作*/
	.p-footer {
		margin: 30rpx;
		display: flex;
		font-size: 24rpx;
		color: #616161;

		.p-item {
			display: flex;
			align-items: center;

			.icon {
				margin-right: 10rpx;
			}

			&:nth-child(2) {
				margin: 0 auto;
			}

			.iconfont {
				margin-right: 10rpx;
			}
		}
	}

	/*评论列表*/
	.comment-box {
		background-color: #ffffff;
		margin-top: 20rpx;
		padding: 20rpx;
	}

	.comment-box>.title {
		margin-bottom: 20rpx;
	}

	.comment-item {
		display: flex;
		padding: 20rpx;

		&:active {
			background-color: #f5f5f5;
		}

		.c-content {
			display: flex;
			flex-direction: column;
			flex: 1;

			.time {
				color: #999;
				font-size: 10px;
			}

			.user {
				display: flex;

				.thumbs {
					margin-left: auto;
					display: flex;
					align-items: center;
					color: #bfbfbf;

					.num {
						margin-right: 10rpx;
					}
				}
			}
		}

		.avatar {
			width: 100rpx;
			height: 100rpx;
			border-radius: 50%;
			margin-right: 20rpx;
		}
	}

	.sub-comment-item {
		margin-left: 100rpx;
		padding: 20rpx;

		&:active {
			background-color: #f5f5f5;
		}

		.user {
			display: flex;
			align-items: center;

			.name {
				color: #616161;
			}

			.avatar {
				margin-right: 10rpx;
			}

			.u-head {
				flex: 1;
				display: flex;

				.thumbs {
					margin-left: auto;
					display: flex;
					align-items: center;
					color: #bfbfbf;

					.num {
						margin-right: 10rpx;
					}
				}
			}
		}

		.reply {
			.time {
				margin-left: auto;
				font-size: 20rpx;
				color: #999;
			}

			.name {
				display: inline-block;
				color: #2b85e4;
				font-weight: 600;
			}
		}
	}

	/* 评论tool */
	.comment-tool {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		padding: 20rpx;
		background-color: #fff;
		display: flex;
		z-index: 999;
	}

	.comment-tool textarea {
		background-color: #f5f5f5;
		padding: 20rpx;
		border-radius: 10rpx;
		min-height: 40rpx;
	}

	.comment-tool .u-btn {
		margin-left: 10rpx;
	}

	// 圈子信息
	.topic-info {
		display: flex;
		align-items: center;
		font-size: 24rpx;
		background-color: #f5f5f5;
		margin: 30rpx;
		padding: 20rpx;
		border-radius: 20rpx;

		.cover {
			width: 100rpx;
			height: 100rpx;
			margin-right: 20rpx;
			border-radius: 10rpx;
		}

		.center {
			flex: 1;

			.count-txt {
				color: #999;
			}
		}

		.right {
			margin-left: 20rpx;
			background-color: $themes-color;
			padding: 10rpx 30rpx;
			border-radius: 50rpx;
			color: #fff;
		}
	}

	// 分享弹窗
	.share-wrap {
		display: flex;
		padding: 30rpx;
		width: 60%;
		margin: 0 auto;

		.share-item {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;

			&:nth-child(1) {
				margin-right: auto;
			}

			image {
				width: 100rpx;
				height: 100rpx;
			}

			text {
				font-size: 24rpx;
				margin-top: 20rpx;
			}
		}
	}

	//海报弹窗
	.canvas-box {
		height: 560px;
		position: relative;

		.footer {
			position: absolute;
			bottom: 20rpx;
			left: 20rpx;
			right: 20rpx;
		}
	}

	// 投票
	.vote-box {
		background-color: #f5f5f5;
		border-radius: 20rpx;
		padding: 20rpx;
		margin: 20rpx;

		.title {
			font-weight: bold;
		}

		.expire-time {
			font-size: 24rpx;
			margin: 20rpx 0;
		}

		.vote-item {
			font-size: 24rpx;
			font-weight: bold;
			padding: 20rpx;
			border-radius: 20rpx;
			border: 1px solid #999;
			text-align: center;
			margin-bottom: 20rpx;

			&:last-child {
				margin-bottom: 0;
			}
		}

		.active {
			background-color: #333;
			color: #fff;
		}
	}

	.expire-box {
		background-color: #999;
		color: #fff;
		font-size: 24rpx;
		display: inline-block;
		padding: 0 20rpx;
		border-radius: 10rpx;
		margin-bottom: 20rpx;
	}

	video {
		width: 100%;
	}

	// 轮播图指示器
	.indicator-num {
		padding: 2px 0;
		background-color: rgba(0, 0, 0, 0.35);
		border-radius: 100px;
		width: 35px;
		@include flex;
		justify-content: center;

		&__text {
			color: #FFFFFF;
			font-size: 12px;
		}
	}

	.post-content-wrap {
		padding: 30rpx;

		.discuss-item {
			display: inline-block;
			color: #2b85e4;
		}
	}
</style>
