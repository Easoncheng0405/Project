package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.*;
import com.jlu.chengjie.model.form.FormSavings;
import com.jlu.chengjie.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/*
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/28
 */


/**
 * 网上银行controller
 */
@Controller
@RequestMapping("/bank")
public class NetBankController {

    private final SavingRepository savingRepository;

    private final RecordRepository recordRepository;

    private final CardRepository cardRepository;

    private final CardRecordRepository cardRecordRepository;

    private final AccountRepository accountRepository;

    @Autowired
    public NetBankController(SavingRepository savingRepository, RecordRepository recordRepository,
                             CardRepository cardRepository, CardRecordRepository cardRecordRepository,
                             AccountRepository accountRepository) {
        this.savingRepository = savingRepository;
        this.recordRepository = recordRepository;
        this.cardRepository = cardRepository;
        this.cardRecordRepository = cardRecordRepository;
        this.accountRepository = accountRepository;
    }


    @GetMapping
    public String get(@ModelAttribute String success, HttpSession session, @RequestParam(required = false) String range, Model model) throws ParseException {

        Account account = (Account) session.getAttribute("CURRENT_ACCOUNT");

        //未拿到当前账户，跳转到登陆页面
        if (account == null)
            return "redirect:/login";

        model.addAttribute("curr_user", "当前使用的一卡通账号: " + account.getId() + "所有人姓名: " + account.getName() + " 所有人身份证号: " + account.getPid());

        List<Record> records;
        if (range == null || range.equals(""))
            records = recordRepository.findByAccount(account);
        else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date st = format.parse(range.substring(0, 19));
            Date ed = format.parse(range.substring(22, 41));

            if (st.after(ed)) {
                model.addAttribute(Constant.MESSAGE, "起始时间应该在结束时间之前");
                records = recordRepository.findByAccount(account);
            } else {
                records = recordRepository.findByAccountAndDateBetween(account, st, ed);
                model.addAttribute("sss", "已显示所有" + st + "到" + ed + "间的交易记录");
            }
        }


        model.addAttribute("records", records);

        List<FormSavings> savings = new ArrayList<>();

        List<CardRecord> crs = cardRecordRepository.findAll();

        model.addAttribute("crs", crs);
        for (Savings s : savingRepository.findByEnableAndAccount(true, account))
            savings.add(FormSavings.getForm(s));

        model.addAttribute("savings", savings);
        return "bank";
    }


    @GetMapping("/data")
    @ResponseBody
    public HashMap<String, Object> getData(HttpSession session) {

        Account account = (Account) session.getAttribute("CURRENT_ACCOUNT");

        HashMap<String, Object> map = new HashMap<>();

        List<Savings> savings = savingRepository.findByEnableAndAccount(true, account);

        ArrayList<String> label = new ArrayList<>(savings.size());

        ArrayList<double[][]> res = new ArrayList<>(savings.size());

        ArrayList<Double> donut = new ArrayList<>();
        for (Savings s : savings) {

            label.add(s.getId() + "");

            List<Record> records = recordRepository.findByS(s);
            double[][] data = new double[records.size()][2];


            double tmp = s.getMoney().doubleValue();

            for (int i = 0; i < records.size(); i++) {

                double v = records.get(i).getMoneyEnd().doubleValue();

                data[i][0] = i;
                data[i][1] = v;

            }

            donut.add(tmp);
            res.add(data);
        }

        map.put("label", label);
        map.put("res", res);
        map.put("donut", donut);
        return map;
    }


    @PostMapping("/card")
    public String newCard(@RequestParam long id, @RequestParam String password, final RedirectAttributes attributes
            , HttpSession session) {
        Account account = (Account) session.getAttribute("CURRENT_ACCOUNT");

        //未拿到当前账户，跳转到登陆页面
        if (account == null)
            return "redirect:/login";


        if (cardRepository.findById(id) != null) {
            attributes.addFlashAttribute("success", "这张卡已经开通了");
            return "redirect:/bank";
        }

        BigDecimal zero = new BigDecimal(0);

        Card card = new Card();
        card.setId(id);
        card.setAccount(account);
        card.setMoney(Constant.CARD);
        card.setPassword(password);
        card.setType("正常");
        card.setV(zero);
        card.setBorrow(zero);
        card.setClast(zero);
        card.setTemp(zero);
        card.setClate(zero);
        card.setCindex(0);
        card.setDate(new Date());
        card.setTag(0);

        cardRepository.save(card);
        attributes.addFlashAttribute("success", "成功开通信用卡");
        return "redirect:/bank";
    }

    @PostMapping("/lost")
    public String lost(@RequestParam String type, @RequestParam String pid, final RedirectAttributes attributes,
                       HttpSession session) {

        Account account = (Account) session.getAttribute("CURRENT_ACCOUNT");

        if (!account.getPid().equals(pid)) {
            attributes.addFlashAttribute(Constant.MESSAGE, "身份证号码不正确");
            return "redirect:/bank";
        }
        account.setState(type);
        session.removeAttribute("CURRENT_ACCOUNT");

        accountRepository.save(account);

        return "redirect:/login";
    }

}
