package com.socialCircle.interceptor;


import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.socialCircle.common.JWTUtil;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT验证拦截器
 */

public class JWTInterceptor implements HandlerInterceptor {


    private JWTUtil jwtUtil;
    public JWTInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Result<Object> r = null;
        //令牌建议是放在请求头中，获取请求头中令牌
        String token = request.getHeader("authorization");
        if (token != null) {
            try {
                jwtUtil.verify(token);//验证令牌
                User user = new User();
                DecodedJWT t = jwtUtil.getToken(token);
                user.setId(t.getClaim("id").as(Integer.class));
                user.setPermission(t.getClaim("permission").as(Integer.class));
                request.setAttribute("user", user);
                return true;//放行请求
            } catch (SignatureVerificationException e) {
                r = Result.error("无效签名");
            } catch (TokenExpiredException e) {
                r = Result.error("token过期");
            } catch (AlgorithmMismatchException e) {
                r = Result.error("token算法不一致");
            } catch (Exception e) {
                r = Result.error("token失效");
            }
        }else {
            r = Result.error("请登录");
        }
        //将map转化成json，response使用的是Jackson
        String json = JSON.toJSONString(r);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(json);
        return false;
    }
}