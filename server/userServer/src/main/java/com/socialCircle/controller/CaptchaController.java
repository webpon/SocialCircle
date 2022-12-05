package com.socialCircle.controller;


import cn.hutool.core.lang.UUID;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * 验证码Controller
 * 主要生成验证码
 * 获取验证码 String code = (String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
 */
@Controller
@RequestMapping("/kaptcha")
public class CaptchaController {

    @Autowired
    private Producer captchaProducer;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping
    @ResponseBody
    public Result getKaptchaImagKey(){
        String capText = captchaProducer.createText();
        String key = UUID.randomUUID().toString().substring(15);
        redisUtil.save(RedisKey.IMAGE_CODE + key, capText, 10, TimeUnit.MINUTES);
        return Result.ok("ok", key);
    }

    @GetMapping("/image")
    public ModelAndView getKaptchaImage(String key, HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String bean = redisUtil.getBean(RedisKey.IMAGE_CODE +key, String.class);
        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(bean);
        ServletOutputStream out = response.getOutputStream();
        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }
}

