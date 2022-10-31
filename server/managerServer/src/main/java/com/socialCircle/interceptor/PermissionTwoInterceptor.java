package com.socialCircle.interceptor;

import com.alibaba.fastjson.JSON;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PermissionTwoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getAttribute("user");
        if (user.getId() == 1 || user.getPermission()  > 0 ) {
            return true;
        }
        String json = JSON.toJSONString(Result.error("权限不足"));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(json);
        return false;
    }

}
