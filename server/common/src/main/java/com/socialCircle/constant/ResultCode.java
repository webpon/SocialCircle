package com.socialCircle.constant;

public interface ResultCode {
    //通用成功返回值
    Integer SUCCEED = 2000;
    // 登陆成功
    Integer LOGIN_OK = 2001;
    // 登录失败
    Integer LOGIN_FALL = 2002;
    // 注册成功
    Integer SIGN_IN_OK = 2004;
    // 账号被封
    Integer LOGIN_ERROR = 4040;
    // 账号已被登录
    Integer LOGIN_LOGGED_IN = 4002;
    // 注册失败
    Integer SIGN_IN_FALL = 4003;

    // 有数据
    Integer HAVE_DATA = 2003;
    // 没有数据
    Integer NOT_HAVE_DATA = 4001;
}
