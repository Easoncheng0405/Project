package com.jlu.chengjie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/28
 */
@Controller
@RequestMapping("login")
public class LoginController {


    @GetMapping
    public String get(){
        return "login";
    }


    @PostMapping
    public String post(@RequestParam("id")int id,@RequestParam("password")String password){
        return "redirect:/admin";
    }

}