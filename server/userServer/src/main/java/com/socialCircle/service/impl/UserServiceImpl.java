package com.socialCircle.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.common.JWTUtil;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.common.SentSimpleMail;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.ResultCode;
import com.socialCircle.dao.UserDao;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.service.UserInfoService;
import com.socialCircle.service.UserService;
import com.socialCircle.vm.UserInfoVM;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.socialCircle.constant.RedisKey.EMAIL_CODE;
import static com.socialCircle.constant.RedisKey.LOGIN;

@Log4j
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

    public Result ifEmailCode(String email, String emailCode){
        String code = redisUtil.getBean(EMAIL_CODE + email, String.class);
        if (code == null) {
            return Result.error(ResultCode.CODE_ERROR,"验证码时效");
        }
        if (!code.equals(emailCode)){
            return Result.error(ResultCode.CODE_ERROR,"验证码失败");
        }
        redisUtil.delete(EMAIL_CODE + email);
        return null;
    }

    /**
     * 登录
     *
     */
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
            userDao.loginTime(login);
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
    public Result signIn(SignIn signIn) {
        Result result = ifEmailCode(signIn.getEmail(), signIn.getEmailCode());
        if (result != null){
            return result;
        }
        User user = userDao.queryByEmail(signIn.getEmail());
        if (user != null){
            return Result.error("注册失败");
        }
        signIn.setPermission(0);
        synchronized (this) {
            // 生成账号
            long maxId = userDao.getMaxId() + 1L;
            StringBuilder stringBuffer = new StringBuilder(Long.toString(maxId));
            for (int i = stringBuffer.length(); i < 10; i++) {
                stringBuffer.insert(0, "0");
            }
            signIn.setAccountId(stringBuffer.substring(0));
        }
        signIn.setPassword(md5(signIn.getPassword()));
        // 添加成功
        if (userDao.save(signIn)) {
            signIn.setId(userDao.queryByEmail(signIn.getEmail()).getId());
            signIn.setGender(1);
            userInfoService.save(signIn);
            signIn.setPassword(null);
            HashMap<String, String> map = new HashMap<>();
            map.put("id",signIn.getId().toString());
            map.put("permission", "0");
            // 获取token
            Object token = jwtUtil.getToken(map);
            return Result.ok("注册成功",token);
        }
        return Result.error("注册失败");
    }


    /**
     * 邮箱登录
     *
     * @param email     邮箱
     * @param emailCode 验证码
     */
    @Override
    public Result login(String email, String emailCode) {
        Result result = ifEmailCode(email,emailCode);
        if (result != null){
            return result;
        }
        User login = userDao.queryByEmail(email);
        // 判断是已经登录和是否被封号
        if (!redisUtil.setIfAbsent(LOGIN+login.getId())) {
            return  Result.error("账号已被登录");
        }
        if (login.getBanned() == 1){
            return Result.error("已经被封");
        }
        // 设置为登录
        userDao.loginTime(login);
        HashMap<String, String> map = new HashMap<>();
        map.put("id",login.getId().toString());
        map.put("permission", login.getPermission().toString());
        // 获取token
        Object token = jwtUtil.getToken(map);
        return Result.ok(token);
    }

    /**
     * 忘记密码，修改密码
     *
     * @param signIn |
     */
    @Override
    public Result forgetPassword(SignIn signIn) {
        Result result = ifEmailCode(signIn.getEmail(), signIn.getEmailCode());
        if (result != null){
            return result;
        }
        signIn.setId(userDao.selectOne(
                new QueryWrapper<User>()
                        .eq("email", signIn.getEmail())
        ).getId());
        signIn.setPassword(md5(signIn.getPassword()));
        if (userDao.updatePassword(signIn)){
            return Result.ok("修改成功");
        }
        return Result.error("修改失败");
    }

    /**
     * 修改密码
     */
    @Override
    public Result updatePassword(SignIn signIn, User user) {
        String md5 = md5(signIn.getPassword());
        User user1 = userDao.selectById(user.getId());
        if (user1.getPassword().equals(md5)){
            user.setPassword(md5(signIn.getNewPassword()));
            int i = userDao.updateById(user);
            if (i > 0){
                return Result.ok("修改成功");
            }
        }
        return Result.error("修改失败");
    }


    private String md5(String s){
        // 密码加密5次
        for (int i = 0; i < 5; i++) {
            s = SecureUtil.md5(s);
        }
        return s;
    }

}
