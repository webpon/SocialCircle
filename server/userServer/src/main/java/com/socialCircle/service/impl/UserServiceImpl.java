package com.socialCircle.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;
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
    @Resource
    private SentSimpleMail sentSimpleMail;

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
            // 设置为登录
            redisUtil.setIfAbsent(LOGIN+user.getId());
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
        String code = redisUtil.getBean(EMAIL_CODE + signIn.getEmail(), String.class);
        if (code == null) {
            return Result.error(ResultCode.CODE_ERROR,"验证码时效");
        }
        if (!code.equals(signIn.getEmailCode())){
            return Result.error(ResultCode.CODE_ERROR,"验证码失败");
        }
        User user = userDao.queryByEmail(signIn.getEmail());
        if (user != null){
            return Result.error("注册失败");
        }
        signIn.setPermission(0);
        // 生成账号
        long maxId = userDao.getMaxId()+1L;
        StringBuilder stringBuffer = new StringBuilder(Long.toString(maxId));
        for (int i = stringBuffer.length(); i < 10; i++) {
            stringBuffer.insert(0,"0");
        }
        signIn.setAccountId(stringBuffer.substring(0));
        signIn.setPassword(md5(signIn.getPassword()));
        // 添加成功
        if (userDao.save(signIn)) {
            signIn.setId(userDao.queryByEmail(signIn.getEmail()).getId());
            signIn.setPetName(UUID.fastUUID().toString().substring(0,10));
            signIn.setGender(1);
            userInfoService.save(signIn);
            signIn.setPassword(null);
            redisUtil.delete(EMAIL_CODE + signIn.getEmail());
            return Result.ok("注册成功",signIn);
        }
        return Result.error("注册失败");
    }

    /**
     * 发送验证码
     *
     * @param sessionCode 正确验证码
     * @param email       用户邮箱
     * @param code        用户输入的验证码
     */
    @Override
    public Result emailCode(String sessionCode, String email, String code) {
        if (!sessionCode.equals(code)){
            return Result.error(ResultCode.CODE_ERROR,"验证码错误");
        }
        User user = userDao.queryByEmail(email);
        if (user != null) {
            return Result.ok("当前邮箱被注册");
        }
        String yan = String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, 5)));
        log.debug("当前验证码是:"+yan);
        redisUtil.save(RedisKey.EMAIL_CODE+email, yan,10, TimeUnit.MINUTES);
        String text = "  您正在正在注册圈子社区， 验证码为:"+yan+"。\n如果非被人注册请忽略。";
        try {
            sentSimpleMail.sentSimpleMail("圈子事情验证码", text,email);
        } catch (MessagingException e) {
            log.error(e);
            return Result.error(ResultCode.CODE_ERROR,"验证码错误");
        }
        return Result.ok();
    }
//    @Override
//    public Result userInfo(User user) {
//        UserInfoVM info = userDao.getInfo(user.getId());
//        if (info == null) {
//            return Result.error("没有当前用户");
//        }
//
//        return Result.ok(info);
//    }


    private String md5(String s){
        // 密码加密5次
        for (int i = 0; i < 5; i++) {
            s = SecureUtil.md5(s);
        }
        return s;
    }
}
