package com.socialCircle.common;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisQuery;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 获取单个对象
     * @param key 保存的key
     * @param t 对象
     * @return 得到的对象
     */
    public <T> T getBean(String key, Class<T> t){
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s == null) {
            return null;
        }
        return JSON.parseObject(s, t);
    }

    /**
     * 获取多个对象列表
     * @param key 保存的key
     * @param t 对象
     * @return 得到的对象列表
     */
    public <T> List<T> getBeans(String key, Class<T> t){
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s == null) {
            return null;
        }
        return JSON.parseArray(s, t);
    }

    /**
     * 数据库延时查询
     * @param redisQuery 数据
     * @param redisCommand 查询方法
     */
    public <T> T getBean(RedisQuery redisQuery, RedisCommand redisCommand, Class<T> clazz){
        String key = redisQuery.getPrefixKey() + redisQuery.getSuffixKey();
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s == null) {
            // 开启线程查询
            threadPoolExecutor.execute(()-> {
                // 获取锁
                if (lock(key, 10, TimeUnit.SECONDS)) {
                    redisCommand.run(key);
                    delete(key);
                }
            });
            return null;
        }
        // 转换对象
        RedisQuery redisQuery1 = JSON.parseObject(s, RedisQuery.class);
        long time = redisQuery1.getDate().getTime();
        String now = DateUtil.now();
        long l = DateUtil.parse(now).getTime();
        // 判断是否过期
        if (time < l) {
            threadPoolExecutor.execute(()-> {
                // 获取锁
                if (lock(key, 10, TimeUnit.SECONDS)) {
                    redisCommand.run(key);
                    delete(key);
                }
            });
        }
        return (T) redisQuery1.getData();
    }

    /**
     * 数据库延时查询
     * @param redisQuery 数据
     * @param redisCommand 查询方法
     */
    public <T> List<T> getBeans(RedisQuery redisQuery, RedisCommand redisCommand, Class<T> clazz){
        String key = redisQuery.getPrefixKey() + redisQuery.getSuffixKey();
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s == null) {
            threadPoolExecutor.execute(()-> {
                if (lock(key, 10, TimeUnit.SECONDS)) {
                    redisCommand.run(key);
                    delete(key);
                }
            });
            return null;
        }
        RedisQuery redisQuery1 = JSON.parseObject(s, RedisQuery.class);
        long time = redisQuery1.getDate().getTime();
        String now = DateUtil.now();
        long l = DateUtil.parse(now).getTime();
        if (time < l) {
            threadPoolExecutor.execute(()-> {
                if (lock(key, 10, TimeUnit.SECONDS)) {
                    redisCommand.run(key);
                    delete(key);
                }
            });
        }
        return (List<T>) redisQuery1.getData();
    }

    /**
     * 保存到缓存 默认保存30分钟
     * @param key 保存的key
     * @param t 保存的对象
     */
    public <T> void save(String key,T t) {
        String s = JSON.toJSONString(t);
        stringRedisTemplate.opsForValue().set(key,s,30, TimeUnit.MINUTES);
    }

    /**
     * 指定时间保存
     * @param key 保存的key
     * @param t 保存的对象
     * @param timeout 时长
     * @param time 单位
     */
    public <T> void save(String key,T t, Integer timeout, TimeUnit time) {
        String s = JSON.toJSONString(t);
        stringRedisTemplate.opsForValue().set(key,s, timeout, time);
    }

    /**
     * 没有就保存 默认保存一天
     * @param key 保存的key
     * @return 是否成功
     */
    public Boolean setIfAbsent(String key) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key,"1", 1L, TimeUnit.DAYS);
    }

    /**
     * 删除缓存
     * @param key 保存的key
     * @return 是否删除成功
     */
    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    /**
     * 批量删除缓存
     * @param key 正则的key
     * @return 是否删除成功
     */
    public Boolean batchDelete(String key) {
        Set<String> keys = stringRedisTemplate.keys(key);
        Long delete = stringRedisTemplate.delete(keys);
        return delete > 0;
    }


    /**
     * 分布式锁
     * @param key 保存的key
     * @return 获取所是否成功
     */
    public Boolean lock(String key, Integer timeout, TimeUnit time){
        return stringRedisTemplate.opsForValue().setIfAbsent(key,"1", timeout, time);
    }
}
