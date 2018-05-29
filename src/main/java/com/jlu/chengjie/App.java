package com.jlu.chengjie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/28
 */

@SpringBootApplication
@Controller
public class App {


    public static void main(String[] args){

        SpringApplication.run(App.class,args);
    }


    @GetMapping("/")
    public String get(){

        return "index";
    }

}
