package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.User;
import com.jlu.chengjie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/28
 */
@Controller
@RequestMapping("/login")
public class LoginController {


    private final UserRepository userRepository;

    @Autowired
    public LoginController(UserRepository repository) {
        this.userRepository = repository;
    }

    @GetMapping
    public String get(){

        return "login";
    }


    @PostMapping
    public String post(Model model, @RequestParam long id,@RequestParam String password,HttpSession session){

        User user= userRepository.findByPidAndPassword(id,password);

        if(user==null){
            model.addAttribute("message","用户名或密码错误");
            return "login";
        }

        session.setAttribute("CURRENT_USER",user);

        return "redirect:/";
    }

}
