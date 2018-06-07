package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.model.Plan;
import com.jlu.chengjie.repository.PlanRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
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

    public AdminController(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @GetMapping
    public String get(Model model) {

        List<Plan> plans = planRepository.findAll();

        model.addAttribute("plans", plans);

        model.addAttribute("v1", Constant.V_ONE);
        model.addAttribute("v2", Constant.V_TWO);
        model.addAttribute("card", Constant.V_CARD);

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


}
