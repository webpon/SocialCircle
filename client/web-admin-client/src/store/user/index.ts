import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { RootState } from '..';
import {
  login as loginApi,
  getUserInfo as getUserInfoApi,
  getUserList as getUserListApi,
  getAdminList as getAdminListApi,
  logout as logoutApi
} from 'apis/user'

const namespace = 'user';
const TOKEN_NAME = 'token';

const initialState = {
  token: localStorage.getItem(TOKEN_NAME) || 'main_token', // 默认token不走权限
  userInfo: {
    email: '',
    password: '',
    petName: '',
    gender: 1,
    permission: 2,
    headIcon: ''
  },
  userListData: {
    contractList: [],
    current: 1,
    loading: false,
    pageSize: 10,
    total: 0,
  },
  adminListData: {
    contractList: [],
    current: 1,
    loading: false,
    pageSize: 10,
    total: 0,
  },
  reportLis:{
    list:[],
    current: 1,
    loading: false,
    pageSize: 10,
    total: 0
  }
};

// login
export const login = createAsyncThunk(`${namespace}/login`, async (params: { accountId: string; password: string }) => {
  try {
    const res = await loginApi(params)
    console.log(res);

    return res.data
  } catch (error: any) {
    return Promise.reject(error.msg);
  }
});

// logout
export const logout = createAsyncThunk(`${namespace}/logout`, async () => {
  try {
    const res = await logoutApi()
    return res.data
  } catch (error: any) {
    return Promise.reject(error.msg);
  }
});

// getUserInfo
export const getUserInfo = createAsyncThunk(`${namespace}/getUserInfo`, async () => {
  const res = await getUserInfoApi();
  return res.data;
});

// getUserList
export const getUserList = createAsyncThunk(`${namespace}/getUserList`, async (params: {p: number, q?:string, }) => {

  const res = await getUserListApi(params);
  return {
    data: res.data,
    current: params.p,
    total: res.total
  }
});

// getAdminList
export const getAdminList = createAsyncThunk(`${namespace}/getAdminList`, async (params: {p: number, q?:string, }) => {
  const res = await getAdminListApi(params);
  return {
    data: res.data,
    current: params.p,
    total: res.total
  }
});


const userSlice = createSlice({
  name: namespace,
  initialState,
  reducers: {
    remove: (state) => {
      state.token = '';
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(login.rejected, (state, { error = {} }) => {
        throw new Error(error.message);
      })
      .addCase(login.fulfilled, (state, action) => {
        localStorage.setItem(TOKEN_NAME, action.payload);
        state.token = action.payload;
      })
      .addCase(logout.fulfilled, (state, action) => {
        localStorage.removeItem(TOKEN_NAME);
        state.token = '';
        state.userInfo = {};
      })
      /* 获取当前用户信息 */
      .addCase(getUserInfo.fulfilled, (state, action) => {
        state.userInfo = action.payload;
      })
      /* 获取用户列表 */
      .addCase(getUserList.pending, (state) => {
        state.userListData.loading = true;
      })
      .addCase(getUserList.fulfilled, (state, action) => {
        state.userListData.contractList = action.payload.data;
        state.userListData.current = action.payload.current;
        state.userListData.total = action.payload.total;
        state.userListData.loading = false;
      })
      .addCase(getUserList.rejected, (state, action) => {
        state.userListData.loading = false;
      })
      /* 获取管理列表 */
      .addCase(getAdminList.pending, (state) => {
        state.adminListData.loading = true;
      })
      .addCase(getAdminList.fulfilled, (state, action) => {
        state.adminListData.contractList = action.payload.data;
        state.adminListData.current = action.payload.current;
        state.adminListData.total = action.payload.total;
        state.adminListData.loading = false;
      })
      .addCase(getAdminList.rejected, (state, action) => {
        state.adminListData.loading = false;
      });
  },
});

export const selectListBase = (state: RootState) => state.listBase;
export const selectUserList = (state: RootState) => state.user.userListData;
export const selectAdminList = (state: RootState) => state.user.adminListData;
export const selectReportList = (state: RootState) => state.user.reportLis;
export const selectUserInfo = (state: RootState) => state.user.userInfo;
export const selectToken = (state: RootState) => state.user.token;

export const { remove } = userSlice.actions;

export default userSlice.reducer;
