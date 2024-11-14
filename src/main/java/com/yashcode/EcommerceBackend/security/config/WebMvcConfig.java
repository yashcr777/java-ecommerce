package com.yashcode.EcommerceBackend.security.config;

import com.yashcode.EcommerceBackend.interceptor.Intercept1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    Intercept1 intercept1;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(intercept1);
    }
}