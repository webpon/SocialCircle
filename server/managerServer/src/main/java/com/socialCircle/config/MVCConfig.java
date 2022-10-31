package com.socialCircle.config;

import com.socialCircle.common.JWTUtil;
import com.socialCircle.interceptor.AdminInterceptor;
import com.socialCircle.interceptor.JWTInterceptor;
import com.socialCircle.interceptor.PermissionOneInterceptor;
import com.socialCircle.interceptor.PermissionTwoInterceptor;
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
                .excludePathPatterns("/login","/kaptcha", "/loginByEmail","/emailCode")
                .order(0);
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin")
                .order(1);
        registry.addInterceptor(new PermissionOneInterceptor())
                .addPathPatterns("/manage/user","/sealNumber","/report/**")
                .order(1);
        registry.addInterceptor(new PermissionTwoInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/kaptcha", "/loginByEmail","/emailCode")
                .order(1);

    }
}
