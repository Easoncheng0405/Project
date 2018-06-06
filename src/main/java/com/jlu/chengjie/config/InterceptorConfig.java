package com.jlu.chengjie.config;

import com.jlu.chengjie.fifter.LoginInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/30
 */
@Component
public class InterceptorConfig implements WebMvcConfigurer {



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String urls[]=new String[]{"/","/card"};
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns(urls);
    }
}
