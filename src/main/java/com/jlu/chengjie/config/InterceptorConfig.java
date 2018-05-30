package com.jlu.chengjie.config;

import com.jlu.chengjie.fifter.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
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

   // private final UserService service;

   // @Autowired
    //public InterceptorConfig(UserService service){
      //  this.service=service;
   // }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String urls[]=new String[]{"/","/bank","/card"};
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns(urls);
    }
}
