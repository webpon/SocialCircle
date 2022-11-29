package com.socialcircle.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.socialCircle.common.JWTUtil;
import com.socialCircle.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

/**
 * @author buhao
 * @version MyInterceptor.java, v 0.1 2019-10-17 19:21 buhao
 */
@Component
public class MyInterceptor implements HandshakeInterceptor {

    @Autowired
    private JWTUtil jwtUtil;
    /**
     * 握手前
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpHeaders headers = request.getHeaders();
        List<String> tokens = headers.get("authorization");
        String token = tokens.get(0);
        if (token != null) {
            try {
                jwtUtil.verify(token);//验证令牌
                User user = new User();
                DecodedJWT t = jwtUtil.getToken(token);
                user.setId(t.getClaim("id").as(Integer.class));
                user.setPermission(t.getClaim("permission").as(Integer.class));
                attributes.put("user", user);
                return true;//放行请求
            } catch (Exception e) {
            }
        }
        return false;
    }

    /**
     * 握手后
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

}