package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.Account;
import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.repository.AccountRepository;
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

    private final AccountRepository accountRepository;

    @Autowired
    public RegisterController(AccountRepository repository) {
        this.accountRepository = repository;
    }

    @GetMapping
    public String get(Model model){

        model.addAttribute("account",new Account());
        return "register";
    }


    @PostMapping
    public String post(Account account, HttpSession session, Model model){

        if(accountRepository.findByPid(account.getPid())!=null){
            model.addAttribute("account",account);
            model.addAttribute("message","这个身份证号码已经被注册了");
            return "register";
        }


        account.setId(accountRepository.findAll().size()+Constant.ID);
        session.setAttribute("CURRENT_ACCOUNT", accountRepository.save(account));

        return "redirect:/";
    }
}
