package com.jlu.chengjie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/29
 */

@Controller
@RequestMapping("card")
public class CardController {

    @GetMapping
    public String get(){
        return "card";
    }

}
