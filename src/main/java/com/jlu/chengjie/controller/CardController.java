package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.Card;
import com.jlu.chengjie.model.CardRecord;
import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.model.FormCard;
import com.jlu.chengjie.repository.CardRecordRepository;
import com.jlu.chengjie.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/29
 */

@Controller
@RequestMapping("card")
public class CardController {

    private final CardRepository cardRepository;

    private final CardRecordRepository cardRecordRepository;

    @Autowired
    public CardController(CardRepository cardRepository, CardRecordRepository cardRecordRepository) {


        this.cardRepository = cardRepository;
        this.cardRecordRepository = cardRecordRepository;
    }

    @GetMapping
    public String get(@ModelAttribute String message, Model model) {

        List<Long> ids = new ArrayList<>();
        List<FormCard> cards = new ArrayList<>();
        for (Card card : cardRepository.findAll()) {
            //只能选择可使用的卡
            if (card.getType().equals("正常"))
                ids.add(card.getId());
            //必须达到的还款金额
            BigDecimal must = card.getV().add(card.getTemp()).add(card.getClate()).add(card.getClast());
            BigDecimal res = card.getBorrow().multiply(new BigDecimal("0.1")).add(must);


            FormCard formCard = new FormCard(card.getId(), card.getDate(), Constant.CARD.toString(), Constant.CARD.subtract(card.getBorrow()).toString()
                    , card.getBorrow().add(must).toString(), res.toString(), card.getV().toString(), card.getClast().toString(), card.getClate().toString(),
                    card.getType(), card.getCindex());

            cards.add(formCard);
        }
        model.addAttribute("ids", ids);
        model.addAttribute("cards", cards);

        return "card";
    }


    @PostMapping("/one")
    public String one(final RedirectAttributes attributes, @RequestParam long id,
                      @RequestParam String money, @RequestParam String name) {

        Card card = cardRepository.findById(id);

        BigDecimal decimal = new BigDecimal(money);

        if (card.getMoney().compareTo(decimal) < 0) {
            attributes.addFlashAttribute(Constant.MESSAGE, "余额不足");
            return "redirect:/card";
        }

        CardRecord cardRecord = new CardRecord();

        cardRecord.setCard(card);
        cardRecord.setDate(new Date());
        cardRecord.setMoney(decimal);
        cardRecord.setMoneyLeft(card.getMoney().subtract(decimal));
        cardRecord.setType("刷卡消费");
        card.setMoney(card.getMoney().subtract(decimal));
        card.setBorrow(card.getBorrow().add(decimal));
        card.setTag(1);
        cardRepository.save(card);
        cardRecordRepository.save(cardRecord);
        attributes.addFlashAttribute(Constant.MESSAGE, "成功在商家 " + name + " 刷卡" + decimal + " 元 剩余可用额度为: " + card.getMoney());
        return "redirect:/card";
    }


    @PostMapping("/two")
    public String two(final RedirectAttributes attributes, @RequestParam long id,
                      @RequestParam String money, @RequestParam String password) {
        Card card = cardRepository.findById(id);

        if (!card.getPassword().equals(password)) {
            attributes.addFlashAttribute(Constant.MESSAGE, "密码不正确");
            return "redirect:/card";
        }

        BigDecimal decimal = new BigDecimal(money);

        if (decimal.compareTo(card.getMoney().multiply(new BigDecimal("0.7"))) > 0) {
            attributes.addFlashAttribute(Constant.MESSAGE, "最多只能取余额的70% 您的余额为: " + card.getMoney());
            return "redirect:/card";
        }

        CardRecord cardRecord = new CardRecord();

        cardRecord.setCard(card);
        cardRecord.setDate(new Date());
        cardRecord.setMoney(decimal);

        //计算手续费，低于一元按一元计
        BigDecimal tmp = decimal.multiply(Constant.TEMP);
        if (tmp.doubleValue() < 1)
            tmp = new BigDecimal("1");


        cardRecord.setMoneyLeft(card.getMoney().subtract(decimal));
        cardRecord.setType("预借现金");

        //处于待还款状态
        card.setTag(1);
        card.setTemp(tmp);
        card.setBorrow(card.getBorrow().add(decimal));
        card.setMoney(card.getMoney().subtract(decimal));
        cardRepository.save(card);
        cardRecordRepository.save(cardRecord);
        attributes.addFlashAttribute(Constant.MESSAGE, "成功 借款" + decimal + " 元 手续费 " + tmp + " 剩余可用额度为: " + card.getMoney());

        return "redirect:/card";
    }


    @PostMapping("/three")
    public String three(@RequestParam long id, @RequestParam String money, final RedirectAttributes attributes) {

        Card card = cardRepository.findById(id);

        BigDecimal decimal = new BigDecimal(money);

        BigDecimal zero = new BigDecimal("0");

        //必须达到的还款金额
        BigDecimal must = card.getV().add(card.getTemp()).add(card.getClate()).add(card.getClast());

        if (decimal.compareTo(must) < 0) {
            attributes.addFlashAttribute(Constant.MESSAGE, "最低需要还清利息,滞纳金,取现手续费和上期未还部分");
        }

        //先计算本月最低还款  先乘0.1 再加必还部分
        BigDecimal res = card.getBorrow().multiply(new BigDecimal("0.1")).add(must);


        //未达到最低还款
        if (res.compareTo(decimal) > 0) {

            //计算额度
            //先还清闲杂费用
            card.setBorrow(card.getBorrow().subtract(decimal.subtract(must)));

            //重新设置可用额度
            card.setMoney(Constant.CARD.subtract(card.getBorrow()));

            //利息,滞纳金,取现手续费清0
            card.setV(zero);
            card.setClate(zero);
            card.setTemp(zero);
            //将本月最低未还部分计入
            card.setClast(res.subtract(decimal));
            cardRepository.save(card);
        } else {

            //计算额度
            //先还清闲杂费用
            card.setBorrow(card.getBorrow().subtract(decimal.subtract(must)));

            //重新设置可用额度
            card.setMoney(Constant.CARD.subtract(card.getBorrow()));

            //利息,滞纳金,取现手续费清0
            card.setV(zero);
            card.setClate(zero);
            card.setTemp(zero);
            card.setCindex(0);
            //本月最低未还部分也请0
            card.setClate(zero);
            //已还清最低还款
            card.setTag(0);

            cardRepository.save(card);
        }

        attributes.addFlashAttribute(Constant.MESSAGE, "成功还款 :" + decimal + "元 剩余可用额度为 :" + card.getMoney());

        return "redirect:/card";
    }
}
