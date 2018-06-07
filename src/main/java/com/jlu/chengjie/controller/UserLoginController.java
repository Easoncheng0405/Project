package com.jlu.chengjie.controller;
/*
 *个人博客:http://www.chengjie-jlu.com
 *Github:https://github.com/Easoncheng0405
 *作者 程杰
 *创建时间 2018/6/7
 */


import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.model.User;
import com.jlu.chengjie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login/user")
public class UserLoginController {


    private final UserRepository userRepository;

    @Autowired
    public UserLoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String get() {

        if (userRepository.findById(40000L) == null) {
            User user = new User();
            user.setId(40000L);
            user.setPassword("1234abcd*");
            user.setOne("1234abcd*");
            user.setTwo("1234abcd*");
            userRepository.save(user);
        }
        return "sysLogin";
    }

    @PostMapping
    public String post(@RequestParam long id, @RequestParam String password, HttpSession session, Model model) {

        User user = userRepository.findByIdAndPassword(id, password);

        if (user == null) {
            model.addAttribute(Constant.MESSAGE, "工号或密码错误");
            return "sysLogin";
        }

        session.setAttribute("CURRENT_USER", user);

        return "redirect:/admin";
    }


    @PostMapping("/modify")
    public String modify(@RequestParam long pid, @RequestParam String op, @RequestParam String np, final RedirectAttributes attributes) {


        User user = userRepository.findById(pid);

        if (AdminController.match(np)) {
            attributes.addFlashAttribute(Constant.MESSAGE, "新密码不符合规则!");
            return "redirect:/login/user";
        }

        if (user == null) {
            attributes.addFlashAttribute(Constant.MESSAGE, "没有这个账户");
            return "redirect:/login/user";
        }

        if (!user.getPassword().equals(op)) {
            attributes.addFlashAttribute(Constant.MESSAGE, "密码不正确");
            return "redirect:/login/user";
        }

        if (np.equals(op) || np.equals(user.getOne()) || np.equals(user.getTwo())) {
            attributes.addFlashAttribute(Constant.MESSAGE, "不能和前三次密码相同");
            return "redirect:/login/user";
        }

        user.setTwo(user.getOne());
        user.setOne(user.getPassword());
        user.setPassword(np);


        userRepository.save(user);

        return "redirect:/login/user";
    }
}
