package com.socialCircle.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Component
public class ResponseChatUtil {

    @Value("${com.socialCircle.chat.url}")
    private String url;
    @Resource
    private RestTemplate restTemplate;

    public void sendMsg(String key){
        String url = this.url+"/inform?key="+key;
        restTemplate.getForObject(url,String.class);
    }
}
