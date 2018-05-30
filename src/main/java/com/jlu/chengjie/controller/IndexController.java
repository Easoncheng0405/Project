package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.Account;
import com.jlu.chengjie.model.User;
import com.jlu.chengjie.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/30
 */

@Controller
@RequestMapping("/")
public class IndexController {

    private final AccountRepository repository;

    @Autowired
    public IndexController(AccountRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String index(HttpSession session,Model model) {

        List<Account> accounts = repository.findByUser((User) session.getAttribute("CURRENT_USER"));

        Set<Long> ids = new HashSet<>();

        for (Account account : accounts)
            ids.add(account.getNumber());

        model.addAttribute("ids",ids);
        return "redirect:/index";
    }

    @PostMapping("/account")
    public String createAccount(@RequestParam long id, @RequestParam String name,
                                @RequestParam String password, Model model, HttpSession session) {

        if (id < 11000 || id > 53000) {
            model.addAttribute("account", "账号不符合规则");
            return "/index";
        }

        Account account = repository.findByNumber(id);

        if (account != null) {
            model.addAttribute("account", "此账号已使用");
            return "redirect:/index";
        }

        account = new Account();

        account.setDate(new Date());
        account.setMoney(0);
        account.setName(name);
        account.setNumber(id);
        account.setState("正常");
        account.setV(0);
        account.setUser((User) session.getAttribute("CURRENT_USER"));

        repository.save(account);
        return "redirect:/index";

    }


    @PostMapping("/saveOne")
    public String saveOne(@RequestParam long id, @RequestParam double money, @RequestParam String password,
                          Model model, HttpSession session) {


        System.out.println(id+"--"+password+"--"+money);


        return "redirect:/index";
    }
}
