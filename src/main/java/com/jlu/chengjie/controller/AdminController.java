package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.*;
import com.jlu.chengjie.model.form.FormCard;
import com.jlu.chengjie.model.form.FormSavings;
import com.jlu.chengjie.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/28
 */
@Controller
@RequestMapping("/admin")
public class AdminController {


    private final PlanRepository planRepository;

    private final UserRepository userRepository;


    private final SavingRepository savingRepository;

    private final CardRepository cardRepository;

    public AdminController(PlanRepository planRepository, UserRepository userRepository,
                           SavingRepository savingRepository, CardRepository cardRepository) {
        this.planRepository = planRepository;
        this.userRepository = userRepository;
        this.savingRepository = savingRepository;
        this.cardRepository = cardRepository;
    }

    @GetMapping
    public String get(@ModelAttribute String message, Model model, HttpSession session) {

        User user = (User) session.getAttribute("CURRENT_USER");

        if (session.getAttribute("CURRENT_USER") == null)
            return "redirect:/login/user";


        switch ((int) user.getId() / 10000) {
            case 1:
                model.addAttribute("one", "one");
                model.addAttribute("welcome", "您在正在以储蓄贷款部门员工登陆");
                break;
            case 2:
                model.addAttribute("two", "two");
                model.addAttribute("welcome", "您在正在以信用卡部门员工登陆");
                break;
            case 3:
                model.addAttribute("three", "three");
                model.addAttribute("welcome", "您在正在以外汇部门员工登陆");
                break;
            case 4:
                model.addAttribute("sys", "sys");
                model.addAttribute("one", "one");
                model.addAttribute("two", "two");
                model.addAttribute("three", "three");
                model.addAttribute("welcome", "您在正在管理员身份登陆");
                break;
            default:
                break;
        }

        List<Plan> plans = planRepository.findAll();

        model.addAttribute("plans", plans);

        model.addAttribute("v1", Constant.V_ONE);
        model.addAttribute("v2", Constant.V_TWO);
        model.addAttribute("card", Constant.V_CARD);


        List<FormSavings> savings = new ArrayList<>();

        for (Savings s : savingRepository.findAll())
            savings.add(FormSavings.getForm(s));

        model.addAttribute("savings", savings);

        List<FormCard> cards = new ArrayList<>();
        for (Card card : cardRepository.findAll()) {
            //只能选择可使用的卡

            //必须达到的还款金额
            BigDecimal must = card.getV().add(card.getTemp()).add(card.getClate()).add(card.getClast());
            BigDecimal res = card.getBorrow().multiply(new BigDecimal("0.1")).add(must);


            FormCard formCard = new FormCard(card.getId(), card.getDate(), Constant.CARD.toString(), Constant.CARD.subtract(card.getBorrow()).toString()
                    , card.getBorrow().add(must).toString(), res.toString(), card.getV().toString(), card.getClast().toString(), card.getClate().toString(),
                    card.getType(), card.getCindex());

            cards.add(formCard);
        }
        model.addAttribute("cards", cards);
        return "admin";
    }


    @PostMapping
    public String post(@RequestParam String range, @RequestParam double v1, @RequestParam double v2,
                       @RequestParam double card, final RedirectAttributes attributes) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date end = format.parse(range.substring(0, 16));

        if (end.before(new Date())) {
            attributes.addFlashAttribute(Constant.MESSAGE, "新计划的时间必须在当前时间之后");
            return "redirect:/admin";
        }

        Plan plan = new Plan();

        plan.setV1(v1);
        plan.setV2(v2);
        plan.setDate(end);
        plan.setType("等待");
        plan.setCard(card);


        planRepository.save(plan);

        return "redirect:/admin";
    }

    @GetMapping("/start")
    @ResponseBody
    public String start(@RequestParam long id) {

        Plan plan = planRepository.findById(id);

        if (plan.getDate().before(new Date()))
            return "该计划已过期！";

        if (plan.getType().equals("就绪"))
            return "该计划正在进行中！";

        if (planRepository.findByType("就绪") != null)
            return "系统中只能有一个正在进行的计划！";
        plan.setType("就绪");
        planRepository.save(plan);
        return "计划启动成功！";
    }

    @GetMapping("/cancel")
    @ResponseBody
    public String cancel(@RequestParam long id) {

        Plan plan = planRepository.findById(id);

        if (plan.getDate().before(new Date()))
            return "该计划已过期！";

        if (plan.getType().equals("等待"))
            return "该计划未进行！";

        plan.setType("等待");
        planRepository.save(plan);
        return "已取消计划";
    }

    @GetMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam long id) {

        Plan plan = planRepository.findById(id);

        planRepository.delete(plan);
        return "已删除计划！";
    }


    @GetMapping("/user")
    @ResponseBody
    public String newUser(@RequestParam long id, @RequestParam String password) {


        if (id < 10000 || id > 49999) {
            return "账号必须在10000到49999之间";
        }

        if (password.length() < 8 || password.length() > 16) {
            return "密码长度在8~12位之间";
        }

        if (match(password))
            return "密码必须同时包含字母数字和特殊字符";

        if (userRepository.findById(id) != null)
            return "已经存在工号为的" + id + "用户";


        User user = new User();
        user.setId(id);
        user.setPassword(password);

        userRepository.save(user);


        return "成功创建用户";
    }

    @GetMapping("/del")
    @ResponseBody
    public String delUser(@RequestParam long id, HttpSession session) {


        if (id == 40000L)
            return "无法删除超级管理员";

        User user = userRepository.findById(id);


        if (user == null)
            return "没有这个用户";

        userRepository.delete(user);

        if (id == ((User) session.getAttribute("CURRENT_USER")).getId()) {
            session.removeAttribute("CURRENT_USER");
            return "当前用户被删除,请重新登陆";
        } else
            return "成功删除用户！";


    }


    static boolean match(String str) {
        boolean a = false, b = false, c = false;

        for (int i = 0; i < str.length(); i++) {

            int tmp = (int) str.charAt(i);
            if ((tmp >= 65 && tmp <= 90) || (tmp >= 97 && tmp <= 122))
                a = true;
            if (tmp >= 48 && tmp <= 57)
                b = true;
            if (tmp <= 47 && tmp >= 33)
                c = true;
        }

        return !a || !b || !c;
    }

}
