package com.socialCircle.interceptor;


import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.socialCircle.common.JWT;
import com.socialCircle.entity.Result;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT验证拦截器
 */
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    private JWT jwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Result<Object> r = null;
        //令牌建议是放在请求头中，获取请求头中令牌
        String token = request.getHeader("Authorization");
        if (token != null) {
            try {
                jwt.verify(token);//验证令牌
                request.setAttribute("userId", jwt.getToken(token).getClaim("userId").asString());
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