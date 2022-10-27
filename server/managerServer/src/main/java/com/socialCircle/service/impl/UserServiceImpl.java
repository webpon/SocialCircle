package com.socialCircle.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.socialCircle.common.JWTUtil;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.ResultCode;
import com.socialCircle.dao.UserDao;
import com.socialCircle.entity.*;
import com.socialCircle.service.UserInfoService;
import com.socialCircle.service.UserService;
import com.socialCircle.vm.UserInfoVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

import static com.socialCircle.constant.RedisKey.LOGIN;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private JWTUtil jwtUtil;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public Result login(User user) {
        String s = md5(user.getPassword());
        user.setPassword(s);
        User login = userDao.login(user);
        if (login != null){
            // 判断是已经登录和是否被封号
            if (!redisUtil.setIfAbsent(LOGIN+login.getId())) {
                return  Result.error("账号已被登录");
            }
            if (login.getBanned() == 1){
                return Result.error("已经被封");
            }
            // 设置为登录
            redisUtil.setIfAbsent(LOGIN+user.getId());
            HashMap<String, String> map = new HashMap<>();
            map.put("id",login.getId().toString());
            map.put("permission", login.getPermission().toString());
            // 获取token
            Object token = jwtUtil.getToken(map);
            return Result.ok(token);
        }
        return Result.error("登录失败");
    }

    @Override
    public Result userInfo(User user) {
        UserInfoVM info = userDao.getInfo(user.getId());
        if (info == null) {
            return Result.error("没有当前用户");
        }

        return Result.ok(info);
    }

    @Override
    public Result signIn(SignIn signIn) {
        User user = userDao.queryByEmail(signIn.getEmail());
        if (user != null){
            return Result.error("注册失败");
        }
        // 生成账号
        Long maxId = userDao.getMaxId()+1L;
        StringBuilder stringBuffer = new StringBuilder(maxId.toString());
        for (int i = stringBuffer.length(); i < 10; i++) {
            stringBuffer.insert(0,"0");
        }
        signIn.setAccountId(stringBuffer.substring(0));
        signIn.setPassword(md5(signIn.getPassword()));
        // 添加成功
        if (userDao.save(signIn)) {
            signIn.setId(userDao.queryByEmail(signIn.getEmail()).getId());
            userInfoService.save(signIn);
            signIn.setPassword(null);
            return Result.ok("注册成功",signIn);
        }
        return Result.error("注册失败");
    }

    @Override
    public Result updateManagerPermission(User user) {
        if (user.getPermission() == null) {
            return Result.error("修改失败");
        }
        user.setBanned(null);
        if (userDao.updateById(user)) {
            UserInfoVM info = userDao.getInfo(user.getId());
            return Result.ok(info);
        }
        return Result.error("修改失败");
    }

    @Override
    public Boolean banned(SealNumber sealNumber) {
        User user = new User();
        user.setId(sealNumber.getUserId());
        user.setBanned(2);
        redisUtil.delete(LOGIN+user.getId());
        return userDao.updateById(user);
    }

    /**
     * 退出登录
     *
     * @param user
     */
    @Override
    public Result logout(User user) {
        if (redisUtil.delete(LOGIN+user.getId())) {
            return Result.ok();
        }
        return Result.error("退出失败");
    }

    @Override
    public Result deleteManager(Integer id) {
        userInfoService.deleteManager(id);
        if (userDao.deleteManager(id)) {
            return Result.ok("删除成功");
        }
        return Result.ok("删除失败");
    }

    private String md5(String s){
        // 密码加密5次
        for (int i = 0; i < 5; i++) {
            s = SecureUtil.md5(s);
        }
        return s;
    }
}