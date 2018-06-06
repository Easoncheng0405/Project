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
 * 创建时间 2018/5/30
 */

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final AccountRepository accountRepository;

    private final BankAccountRes bankAccountRes;

    @Autowired
    public RegisterController(AccountRepository repository, BankAccountRes bankAccountRes) {
        this.accountRepository = repository;
        this.bankAccountRes = bankAccountRes;
    }

    @GetMapping
    public String get(Model model) {

        model.addAttribute("account", new Account());
        return "register";
    }


    @PostMapping
    public String post(Account account, HttpSession session, Model model) {

        if (accountRepository.findByPid(account.getPid()) != null) {
            model.addAttribute("account", account);
            model.addAttribute("message", "这个身份证号码已经被注册了");
            return "register";
        }


        account.setId(accountRepository.findAll().size() + Constant.ID);
        session.setAttribute("CURRENT_ACCOUNT", accountRepository.save(account));

        return "redirect:/";
    }

    @PostMapping("/netBank")
    public String bank(@RequestParam String name, @RequestParam String password, final RedirectAttributes attributes, HttpSession session) {

        Account account = (Account) session.getAttribute("CURRENT_ACCOUNT");

        if (account == null)
            return "redirect:/login";
        if (!name.matches("^[0-9a-zA-Z]+$")) {
            attributes.addFlashAttribute(Constant.MESSAGE, "用户名只能为字母或数字");
            return "redirect:/card";
        }

        if (bankAccountRes.findByName(name) != null) {
            attributes.addFlashAttribute(Constant.MESSAGE, "这个用户名已经使用了");
            return "redirect:/card";
        }

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccount(account);
        bankAccount.setName(name);
        bankAccount.setPassword(password);
        bankAccountRes.save(bankAccount);

        return "redirect:/bank";
    }
}
