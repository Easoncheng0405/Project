package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.Account;
import com.jlu.chengjie.model.BankAccount;
import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.repository.AccountRepository;
import com.jlu.chengjie.repository.BankAccountRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    private final AccountRepository accountRepository;

    private final BankAccountRes bankAccountRes;

    @Autowired
    public LoginController(AccountRepository repository,BankAccountRes bankAccountRes) {
        this.accountRepository = repository;
        this.bankAccountRes=bankAccountRes;
    }

    @GetMapping
    public String get() {

        return "login";
    }


    @PostMapping
    public String post(Model model, @RequestParam String pid, @RequestParam String password, HttpSession session) {

        Account account = accountRepository.findByPidAndPassword(pid, password);

        if (account == null) {
            model.addAttribute("message", "身份证号或密码错误");
            return "login";
        }

        session.setAttribute("CURRENT_ACCOUNT", account);

        return "redirect:/";
    }

    @PostMapping("/netBank")
    public String bank(@RequestParam String name, @RequestParam String password, final RedirectAttributes attributes){


        if(bankAccountRes.findByNameAndPassword(name,password)==null){
            attributes.addFlashAttribute(Constant.MESSAGE,"用户名或密码错误");
            return "redirect:/";
        }

        return "redirect:/bank";
    }
}
