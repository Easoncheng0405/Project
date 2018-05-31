package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.Account;
import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.model.Record;
import com.jlu.chengjie.model.User;
import com.jlu.chengjie.repository.AccountRepository;
import com.jlu.chengjie.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/30
 */


@Controller
@RequestMapping("/")
public class IndexController {

    private final AccountRepository accountRepository;

    private final RecordRepository recordRepository;

    @Autowired
    public IndexController(AccountRepository accountRepository, RecordRepository recordRepository) {
        this.accountRepository = accountRepository;
        this.recordRepository = recordRepository;
    }

    @GetMapping
    public String index(@ModelAttribute String message, HttpSession session, Model model) {

        initAccount(model, session);
        if (message != null && !message.equals(""))
            model.addAttribute("message", message);
        return "index";
    }

    @PostMapping("/account")
    public String createAccount(@RequestParam long id, @RequestParam String name, @RequestParam String password, Model model,
                                HttpSession session, final RedirectAttributes redirectAttributes) {

        initAccount(model, session);


        if (!password.matches("^\\d{6}$")) {
            model.addAttribute("account", "请使用6位数字密码");
            return "index";
        }

        if (id < 11000 || id > 53000) {
            model.addAttribute("account", "账号不符合规则");
            return "index";
        }

        Account account = accountRepository.findByNumber(id);

        if (account != null) {
            model.addAttribute("account", "此账号已使用");
            return "index";
        }

        account = new Account();

        account.setDate(new Date());
        account.setMoney(0);
        account.setName(name);
        account.setNumber(id);
        account.setState(Constant.ACCOUNT_NORMAL);
        account.setV(0);
        account.setPassword(password);
        account.setUser((User) session.getAttribute("CURRENT_USER"));

        accountRepository.save(account);

        redirectAttributes.addFlashAttribute("message", "已成功创建账户");


        return "redirect:/";

    }


    @PostMapping("saveOne")
    public String saveOne(@RequestParam long id, @RequestParam double money, @RequestParam String password,
                          Model model, HttpSession session, final RedirectAttributes redirectAttributes) {
        initAccount(model, session);

        Account account = accountRepository.findByNumberAndPassword(id, password);

        if (account == null) {
            model.addAttribute("one", "密码不正确！");
            return "index";
        }

        if (money >= 0 && money < 1) {
            model.addAttribute("one", "最低需要存入1元！");
            return "index";
        }


        if (account.getMoney() + money < 0) {
            model.addAttribute("one", "余额不足！");
            return "index";
        }

        //生成一条交易记录
        Record record = new Record();

        record.setAccount(account);
        record.setDate(new Date());
        record.setMoney(money);
        record.setType(Constant.SAVE_ONE);

        //金额变动
        account.setMoney(account.getMoney() + money);

        //存储到数据库
        accountRepository.save(account);
        recordRepository.save(record);
        if (money > 0)
            redirectAttributes.addFlashAttribute("message", ("成功存入活期账户：" + id + " 人民币" + money + " 元"));
        else
            redirectAttributes.addFlashAttribute("message", ("成功从活期账户：" + id + " 取款  人民币" + (0-money) + " 元"));

        return "redirect:/";
    }


    @PostMapping("saveTwo")
    public String saveTwo(@RequestParam long id, @RequestParam double money, @RequestParam String password, Model model,
                          @RequestParam int type, @RequestParam(required = false) String flag, HttpSession session) {

        initAccount(model, session);

        Account account = accountRepository.findByNumberAndPassword(id, password);

        if (account == null) {
            model.addAttribute("one", "密码不正确！");
            return "index";
        }

        if (money >= 0 && money < 100) {
            model.addAttribute("one", "最低需要存入100元！");
            return "index";
        }


        if (account.getMoney() + money < 0) {
            model.addAttribute("one", "余额不足！");
            return "index";
        }


        return "redirect:/";
    }

    private void initAccount(Model model, HttpSession session) {
        List<Account> accounts = accountRepository.findByUser((User) session.getAttribute("CURRENT_USER"));

        List<Long> ids1 = new ArrayList<>();
        List<Long> ids2 = new ArrayList<>();
        List<Long> ids3 = new ArrayList<>();


        for (Account account : accounts) {

            long number = account.getNumber();
            if ((number / 1000) % 10 == 1)
                ids1.add(number);
            if ((number / 1000) % 10 == 2)
                ids2.add(number);
            if ((number / 1000) % 10 == 3)
                ids3.add(number);
        }

        model.addAttribute("ids1", ids1);
        model.addAttribute("ids2", ids2);
        model.addAttribute("ids3", ids3);
    }
}
