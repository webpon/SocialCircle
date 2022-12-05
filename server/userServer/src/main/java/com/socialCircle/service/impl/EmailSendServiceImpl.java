package com.socialCircle.service.impl;

import com.socialCircle.common.JWTUtil;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.common.SentSimpleMail;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.ResultCode;
import com.socialCircle.dao.UserDao;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.EmailSendService;
import com.socialCircle.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EmailSendServiceImpl implements EmailSendService {
    @Resource
    private UserDao userDao;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SentSimpleMail sentSimpleMail;

    private String getCode(String key){
        String code = redisUtil.getBean(RedisKey.IMAGE_CODE+key, String.class);
        redisUtil.delete(RedisKey.IMAGE_CODE+key);
        return code;
    }
    /**
     * 发送验证码
     *
     * @param codeKey 正确验证码
     * @param email       用户邮箱
     * @param code        用户输入的验证码
     */
    @Override
    public Result singInByEmailCode(String codeKey, String email, String code) {
        if (codeKey == null) {
            return Result.error(ResultCode.CODE_ERROR,"请验证码");
        }
        String code1 = getCode(codeKey);
        if (code1 == null) {
            return Result.error(ResultCode.CODE_ERROR,"验证码已过期");
        }
        if (!code1.equals(code)){
            return Result.error(ResultCode.CODE_ERROR,"验证码错误");
        }
        User user = userDao.queryByEmail(email);
        if (user != null) {
            return Result.error("当前邮箱被注册");
        }
        return sendEmail(email,"注册");
    }
    /**
     * 注册发送验证码 登录
     *
     * @param codeKey 正确验证码
     * @param email       用户邮箱
     * @param code        用户输入的验证码
     */
    @Override
    public Result logInByEmailCode(String codeKey, String email, String code) {
        if (codeKey == null) {
            return Result.error(ResultCode.CODE_ERROR,"请验证码");
        }
        String code1 = getCode(codeKey);
        if (code1 == null) {
            return Result.error(ResultCode.CODE_ERROR,"验证码已过期");
        }
        if (!code1.equals(code)){
            return Result.error(ResultCode.CODE_ERROR,"验证码错误");
        }
        User user = userDao.queryByEmail(email);
        if (user == null) {
            return Result.error("当前邮箱没有注册");
        }
        return sendEmail(email, " 登录\\修改密码 ");
    }

    private Result sendEmail(String email, String s){
        String yan = String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, 5)));
        log.debug("当前验证码是:"+yan);
        redisUtil.save(RedisKey.EMAIL_CODE+email, yan,10, TimeUnit.MINUTES);
        String text = "  您正在正在"+s+"圈子社区， 验证码为:"+yan+"。\n如果非被人"+s+"请忽略。";
        try {
            sentSimpleMail.sentSimpleMail("圈子事情验证码", text,email);
        } catch (MessagingException e) {
            log.error(e.toString());
            return Result.error(ResultCode.CODE_ERROR,"验证码错误");
        }
        return Result.ok();
    }
}
