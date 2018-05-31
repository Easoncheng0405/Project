package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.model.User;
import com.jlu.chengjie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/30
 */

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserRepository userRepository;

    @Autowired
    public RegisterController(UserRepository repository) {
        this.userRepository = repository;
    }

    @GetMapping
    public String get(Model model){

        model.addAttribute("user",new User());
        return "register";
    }


    @PostMapping
    public String post(User user, HttpSession session, Model model){

        if(userRepository.findUserByPid(user.getPid())!=null){
            model.addAttribute("user",user);
            model.addAttribute("message","这个号码已经被注册了");
            return "register";
        }


        user.setNumber(userRepository.findAll().size()+Constant.NUMBER);
        session.setAttribute("CURRENT_USER", userRepository.save(user));

        return "redirect:/";
    }
}
