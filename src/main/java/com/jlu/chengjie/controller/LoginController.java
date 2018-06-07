package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.Account;
import com.jlu.chengjie.model.BankAccount;
import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.repository.AccountRepository;
import com.jlu.chengjie.repository.BankAccountRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public LoginController(AccountRepository repository, BankAccountRes bankAccountRes) {
        this.accountRepository = repository;
        this.bankAccountRes = bankAccountRes;
    }

    @GetMapping
    public String get() {

        return "login";
    }


    @PostMapping
    public String post(Model model, @RequestParam String pid, @RequestParam String password, HttpSession session) {

        Account account = accountRepository.findByPidAndPassword(pid, password);

        if (account == null) {

            model.addAttribute(Constant.MESSAGE, "身份证号或密码错误");
            return "login";
        }
        if (!account.getState().equals("正常")) {
            model.addAttribute(Constant.MESSAGE, "已销户或挂失的账户无法登陆！");
            return "login";
        }

        session.setAttribute("CURRENT_ACCOUNT", account);

        return "redirect:/";
    }

    @GetMapping("/netBank")
    @ResponseBody
    public String bank(@RequestParam String name, @RequestParam String password, HttpSession session) {

        Account account = (Account) session.getAttribute("CURRENT_ACCOUNT");
        BankAccount bankAccount = bankAccountRes.findByAccount(account);

        if (bankAccount.getName().equals(name) && bankAccount.getPassword().equals(password))

            return "登陆成功!";
        return "用户名或密码错误";
    }


    @GetMapping("/restart")
    @ResponseBody
    public String restart(@RequestParam String pid) {
        Account account = accountRepository.findByPid(pid);

        if (account == null)
            return "没有这个账户";

        if (account.getState().equals("正常"))
            return "该账户状态正常，无需启用";

        if (account.getState().equals("销户"))
            return "已销户的账户无法启用";

        account.setState("正常");

        accountRepository.save(account);

        return "成功启用该账户！";


    }


}
