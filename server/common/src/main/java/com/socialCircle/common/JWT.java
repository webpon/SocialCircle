package com.socialCircle.common;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Map;

@Component
public class JWT {
    @Value("${com.socialCircle.tokenKey}")
    public String SIGNATURE;

    /**
     * 生成token
     * @param map //传入payload
     * @return 返回token
     */
    public String getToken(Map<String,String> map){
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND,7);
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(SIGNATURE)).toString();
    }

    /**
     * 验证token
     * @param token
     */
    public void verify(String token){
        com.auth0.jwt.JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }

    /**
     * 获取token中payload
     * @param token
     * @return
     */
    public DecodedJWT getToken(String token){
        return com.auth0.jwt.JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }

}
