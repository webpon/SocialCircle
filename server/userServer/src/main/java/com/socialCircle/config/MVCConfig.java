package com.socialCircle.config;

import com.socialCircle.common.JWTUtil;
import com.socialCircle.interceptor.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {
    @Autowired
    private JWTUtil jwtUtil;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptor(jwtUtil))
                .addPathPatterns("/**")
                .excludePathPatterns("/login*","/kaptcha/image","/kaptcha","/signIn","/code/**","/emailCode");
    }
}
