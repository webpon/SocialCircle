package com.socialCircle.controller;


import cn.hutool.core.lang.UUID;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();

        response.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();
        String key = UUID.randomUUID().toString().substring(15);
        redisUtil.save(RedisKey.IMAGE_CODE + key, capText, 10, TimeUnit.MINUTES);
        response.setHeader("Code-Key", key);
        // store the text in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
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

