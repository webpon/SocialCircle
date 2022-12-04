package com.socialCircle.common;

import cn.hutool.core.lang.UUID;
import com.socialCircle.msg.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class ResponseChatUtil {

    @Value("${com.socialCircle.chat.url}")
    private String url;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisUtil redisUtil;

    public void sendMsg(String key){
        String url = this.url+"/inform?key="+key;
        restTemplate.getForObject(url,String.class);
    }

    public void sendMsg(Message message){
        try {
            String key = UUID.randomUUID().toString().substring(10);
            redisUtil.save(key, message, 1, TimeUnit.MINUTES);
            String url = this.url+"/inform?key="+key;
            restTemplate.getForObject(url,String.class);
        } catch (RestClientException e) {
        }
    }
}
