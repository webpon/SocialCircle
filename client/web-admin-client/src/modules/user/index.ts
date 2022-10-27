import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { RootState } from '../store';
import { login as loginApi, getUserInfo as getUserInfoApi, getUserList as getUserListApi, logout as logoutApi } from 'services/user'

const namespace = 'user';
const TOKEN_NAME = 'token';

const initialState = {
  token: localStorage.getItem(TOKEN_NAME) || 'main_token', // 默认token不走权限
  userInfo: {},
  userList: []
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
export const getUserList = createAsyncThunk(`${namespace}/getUserList`, async () => {
  const res = await getUserListApi({
    q: 1,
    p: 1
  });
  return res;
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
      .addCase(getUserInfo.fulfilled, (state, action) => {
        state.userInfo = action.payload;
      })
      .addCase(getUserList.fulfilled, (state, action) => {
        state.userList = action.payload;
      });
  },
});

export const selectListBase = (state: RootState) => state.listBase;
export const selectUserInfo = (state: RootState) => state.user.userInfo;
export const selectToken = (state: RootState) => state.user.token;

export const { remove } = userSlice.actions;

export default userSlice.reducer;
