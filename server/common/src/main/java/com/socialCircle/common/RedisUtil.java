package com.socialCircle.common;

import com.alibaba.fastjson.JSON;
import com.socialCircle.entity.Report;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public <T> T getBean(String key, Class<T> t){
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s == null) {
            return null;
        }
        return JSON.parseObject(s, t);
    }
    public <T> List<T> getBeans(String key, Class<T> t){
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s == null) {
            return null;
        }
        return JSON.parseArray(s, t);
    }

    public <T> void save(String key,T t) {
        String s = JSON.toJSONString(t);
        stringRedisTemplate.opsForValue().set(key,s,30, TimeUnit.MINUTES);
    }
}
