package com.socialCircle.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {

    @Value("${alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${alicloud.access-id}")
    private String accessId;

    @Value("${alicloud.secret-key}")
    private String accessKey;

    @Bean
    public OSS getClient(){
        return new OSSClientBuilder().build(endpoint, accessId, accessKey);
    }
}
