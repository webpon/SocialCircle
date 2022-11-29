<template>
	<view>
		<view class="c-wrap">
			<view class="cell-item">
				<view class="left">头像</view>
				<view class="right">
					<q-alioss-upload :isAvatar="true" :avatarSrc='userInfo.avatar' :maxCount="1" :file="coverMediaList" ref="ossUpload"
						@success="uploadSuccess" :autoUpload="true"></q-alioss-upload>
				</view>
			</view>
			<view class="cell-item">
				<view class="left">昵称</view>
				<view @click="jump(userInfo.username, 'username')" class="right">
					<text>{{userInfo.username}}</text>
					<u-icon name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="cell-item">
				<view class="left">性别</view>
				<view class="right" @click="showGender = true">
					<text>{{userInfo.gender}}</text>
					<u-icon name="arrow-right"></u-icon>
				</view>
			</view>
			<!-- 	<view class="cell-item">
				<view class="left">地区</view>
				<view class="right" @click="showRegion = true">
					<text>{{userInfo.province}} {{userInfo.city}}</text>
					<u-icon name="arrow-right"></u-icon>
				</view>
			</view> -->
			<view class="cell-item">
				<view class="left">生日</view>
				<view class="right" @click="showBirthday = true">
					<text>{{userInfo.birthday | date}}</text>
					<u-icon name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="cell-item">
				<view class="left">个性签名</view>
				<view @click="jump(userInfo.intro, 'intro')" class="right">
					<text>{{userInfo.intro || '这个人很懒，没留下什么'}}</text>
					<u-icon name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="cell-item">
				<view class="left">标签</view>
				<view @click="jump(userInfo.tag_str, 'tag_str')" class="right">
					<view class="tag-box">
						<view v-for="(item, index) in userInfo.tag_str" :key="index" class="tag">{{ item }}</view>
						<view v-if="userInfo.tag_str.length == 0" class="tag">添加标签</view>
					</view>
				</view>
				<u-icon name="arrow-right"></u-icon>
			</view>
		</view>
		<view>
			<q-button @click="outlogin">退出登录</q-button>
		</view>
		<!-- 性别选择 -->
		<u-picker :show="showGender" :columns="gender" keyName="label" @confirm="saveGender"
			@cancel="showGender = false">
		</u-picker>
		<!-- 地区选择 -->
		<u-picker :show="showRegion" mode="region" @confirm="saveRegion" @cancel="showRegion = false"></u-picker>
		<!-- 生日选择器 -->
		<u-datetime-picker :show="showBirthday" mode="date" @confirm="saveBirthday" @cancel="showBirthday = false">
		</u-datetime-picker>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				form: {},
				showGender: false,
				showRegion: false,
				showBirthday: false,
				gender: [
					[{
							label: '男',
							value: 1
						},
						{
							label: '女',
							value: 2
						},
						{
							label: '保密',
							value: 0
						}
					]
				],
				coverMediaList: []
			};
		},
		computed: {
			userInfo() {
				return this.$store.state.userInfo
			}
		},
		methods: {
			uploadSuccess(url) {
				let form = {
					avatar: url
				};

				this.$H.post('user/userInfoEdit', form).then(res => {
					if (res.code == 200) {
						this.$store.state.userInfo.avatar = url;
					}
				});
			},
			// 生日修改
			saveBirthday(e) {
				let timestamp = e.value / 1000
				let date = uni.$u.timeFormat(timestamp, 'yyyy-mm-dd');
				let form = {
					birthday: date
				};

				this.$H.post('user/userInfoEdit', form).then(res => {
					if (res.code == 200) {
						this.$store.state.userInfo.birthday = date;
					}

					this.showBirthday = false;
				});

			},
			// 地区修改
			saveRegion(obj) {
				let province = obj.province.label;
				let city = obj.city.label;

				let form = {
					province: province,
					city: city
				};

				this.$H.post('user/userInfoEdit', form).then(res => {
					if (res.code == 200) {
						this.$store.state.userInfo.province = province;
						this.$store.state.userInfo.city = city;
					}
				});
			},
			// 修改性别
			saveGender(e) {
				let gender = e.value[0].value;

				this.$H
					.post('user/userInfoEdit', {
						gender: gender
					})
					.then(res => {
						if (res.code == 200) {
							this.$store.state.userInfo.gender = e.value[0].label;
						}
					});
			},
			jump(value, type) {
				uni.navigateTo({
					url: 'submit?value=' + JSON.stringify(value) + '&type=' + type
				});
			},
			outlogin() {
				this.$store.commit("logout");
				uni.switchTab({
					url: '/pages/index/index'
				});
			},
			updateAvatar(avatar) {
				this.$H
					.post('user/userInfoEdit', {
						avatar: avatar
					})
					.then(res => {
						if (res.code == 200) {
							this.$store.state.userInfo.avatar = avatar;
						}
					});
			}
		}
	};
</script>
<style lang="scss" scoped>
	.c-wrap {
		padding: 20rpx;
		background-color: #ffffff;
	}

	.bind-mobile {
		display: flex;
	}

	/* 标签 */
	.tag-box {
		display: unset;

		.tag {
			padding: 10rpx 20rpx;
			border-radius: 20rpx;
			font-size: 24rpx;
			display: inline-block;
			margin-right: 20rpx;
			margin-bottom: 20rpx;
			background-color: #ffebe5;
			white-space: nowrap;

			&:nth-child(2n) {
				background-color: #ecf9f2;
			}

			&:nth-child(3n) {
				background-color: #fff7e5;
			}

			&:nth-child(5n) {
				background-color: #b3e0ff;
			}
		}
	}


	.cell-item {
		display: flex;
		align-items: center;
		padding: 30rpx;
		border-bottom: 1px solid #F5F5F5;

		.left {
			margin-right: 20rpx;
			white-space: nowrap;
		}

		.right {
			margin-left: auto;
			color: #616161;
			display: flex;
			align-items: center;

			text {
				margin-right: 10rpx;
			}
		}
	}
</style>
