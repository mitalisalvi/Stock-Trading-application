package com.neu.edu.stocktrading.config;

import com.neu.edu.stocktrading.util.HeaderInterceptorUtil;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer
{
    @Override
    public void addInterceptors(InterceptorRegistry registry) 
    {
        registry.addInterceptor(new HeaderInterceptorUtil());
    }

}