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
    // 权限修改成功
    Integer USER_UPDATE_OK = 2005;
    // 权限修改失败
    Integer USER_UPDATE_FALL = 4004;
    // 用户删除成功
    Integer USER_DELETE_OK = 2006;
    // 用户删除失败
    Integer USER_DELETE_FALL = 4005;
    // 封号成功
    Integer USER_BANNED_OK = 2007;
    // 封号失败
    Integer USER_BANNED_FALL = 4006;
    // 有数据
    Integer HAVE_DATA = 2003;
    // 没有数据
    Integer NOT_HAVE_DATA = 4001;
    // 动态删除成功
    Integer DYNAMIC_DELETE_OK = 2008;
    // 动态删除失败
    Integer DYNAMIC_DELETE_FALL = 4007;
    // 动态保存失败
    Integer DYNAMIC_SAVE_FALL = 4009;
    // 动态保存成功
    Integer DYNAMIC_SAVE_OK = 2009;
    // 话题删除成功
    Integer TOPIC_DELETE_OK = 2010;
    // 话题删除失败
    Integer TOPIC_DELETE_FALL = 4010;
    // 话题保存成功
    Integer TOPIC_SAVE_OK = 2011;
    // 话题保存失败
    Integer TOPIC_SAVE_FALL = 2011;
    
}
