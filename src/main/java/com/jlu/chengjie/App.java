package com.jlu.chengjie;

import com.jlu.chengjie.model.User;
import com.jlu.chengjie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/28
 */

@SpringBootApplication
@EnableScheduling
public class App {


    public static void main(String[] args) {

        SpringApplication.run(App.class, args);

    }


}
