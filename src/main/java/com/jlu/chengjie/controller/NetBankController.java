package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.*;
import com.jlu.chengjie.repository.RecordRepository;
import com.jlu.chengjie.repository.SavingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/28
 */
@Controller
@RequestMapping("/bank")
public class NetBankController {

    private final SavingRepository savingRepository;

    private final RecordRepository recordRepository;

    @Autowired
    public NetBankController(SavingRepository savingRepository, RecordRepository recordRepository) {
        this.savingRepository = savingRepository;
        this.recordRepository = recordRepository;
    }


    @GetMapping
    public String get(HttpSession session, @RequestParam(required = false) String range, Model model) throws ParseException {

        Account account = (Account) session.getAttribute("CURRENT_ACCOUNT");

        //未拿到当前账户，跳转到登陆页面
        if (account == null)
            return "redirect:/login";

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

        for (Savings s : savingRepository.findByEnableAndAccount(true, account)) {

            FormSavings form = new FormSavings();
            form.setId(s.getId());
            form.setDate(s.getDate());
            form.setmType(s.getMoneyType());
            form.setMoney(s.getMoney().toString());
            form.setV(s.getV().toString());
            form.setType(s.getType());
            if (s.getType().equals(Constant.SAVE_TWO))
                if (s.getYear() == 1 || s.getYear() == 2)
                    form.setYear("一年");
                else
                    form.setYear("五年");
            else
                form.setYear("不可用");

            form.setLeft(s.getLeftMoney().toString());
            if (s.getType().equals(Constant.SAVE_TWO))
                if (s.isFlag())
                    form.setFlag("续存");
                else
                    form.setFlag("不续存");
            else
                form.setFlag("不可用");
            if (s.isEnable())
                form.setEnable("可用");
            else
                form.setEnable("失效");

            savings.add(form);
        }

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


            double tmp = 0;

            for (int i = 0; i < records.size(); i++) {

                double v = records.get(i).getMoneyEnd().doubleValue();

                data[i][0] = i;
                data[i][1] = v;

                tmp += v;

            }

            donut.add(tmp);
            res.add(data);
        }

        map.put("label", label);
        map.put("res", res);
        map.put("donut", donut);
        return map;
    }


}
