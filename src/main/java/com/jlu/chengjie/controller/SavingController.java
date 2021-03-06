package com.jlu.chengjie.controller;

import com.jlu.chengjie.model.Account;
import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.model.Record;
import com.jlu.chengjie.model.Savings;
import com.jlu.chengjie.repository.BankAccountRes;
import com.jlu.chengjie.repository.RecordRepository;
import com.jlu.chengjie.repository.SavingRepository;
import com.jlu.chengjie.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 创建时间 2018/5/30
 */


@Controller
@RequestMapping("/")
public class SavingController {


    private final SavingRepository savingRepository;

    private final RecordRepository recordRepository;


    private final BankAccountRes bankAccountRes;

    @Autowired
    public SavingController(SavingRepository savingRepository, RecordRepository recordRepository, BankAccountRes bankAccountRes) {
        this.savingRepository = savingRepository;
        this.recordRepository = recordRepository;
        this.bankAccountRes = bankAccountRes;
    }


    @GetMapping
    public String get(@ModelAttribute String message, Model model, HttpSession session) {

        Account account = (Account) session.getAttribute("CURRENT_ACCOUNT");


        if (account == null)
            return "redirect:/login";

        model.addAttribute("curr_user", "当前使用的一卡通账号: " + account.getId() + "所有人姓名: " + account.getName() + " 所有人身份证号: " + account.getPid());

        if (bankAccountRes.findByAccount(account) == null)
            model.addAttribute("flag", "true");

        List<String> options = new ArrayList<>();
        List<Savings> savings = savingRepository.findByEnableAndAccount(true, account);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //自选项选项
        for (Savings saving : savings)
            options.add(saving.getId() + ":" + saving.getMoney() + "元/" + dateFormat.format(saving.getDate()) + "存入");
        model.addAttribute("options", options);


        /*
         * 六种储蓄选项
         */
        List<String> types = new ArrayList<>();

        types.add(Constant.SAVE_ONE);
        types.add(Constant.SAVE_TWO_FIVE_YEAR_FALSE);
        types.add(Constant.SAVE_TWO_FIVE_YEAR_TRUE);
        types.add(Constant.SAVE_TWO_ONE_YEAR_FALSE);
        types.add(Constant.SAVE_TWO_ONE_YEAR_TRUE);
        types.add(Constant.SAVE_THREE);


        model.addAttribute("types", types);

        return "index";
    }


    /**
     * 存款表单提交
     *
     * @return 重定向页面
     */
    @PostMapping("/save")
    public String newAccount(@RequestParam String mType, @RequestParam String type, @RequestParam String money,
                             @RequestParam String password, HttpSession session, final RedirectAttributes redirectAttributes) {

        Account account = (Account) session.getAttribute("CURRENT_ACCOUNT");

        //未拿到当前账户，跳转到登陆页面
        if (account == null)
            return "redirect:/login";

        if (!account.getPassword().equals(password)) {
            redirectAttributes.addFlashAttribute(Constant.MESSAGE, "密码不正确！");
            return "redirect:/";
        }
        Savings savings = new Savings();
        switch (type) {

            //活期
            case Constant.SAVE_ONE: {

                if (Double.parseDouble(money) < 1) {
                    redirectAttributes.addFlashAttribute(Constant.MESSAGE, "最低存入1元！");
                    return "redirect:/";
                }
                savings.setV(Constant.V_ONE);
                savings.setType(Constant.SAVE_ONE);
                break;
            }
            case Constant.SAVE_TWO_ONE_YEAR_FALSE: {
                if (Double.parseDouble(money) < 100) {
                    redirectAttributes.addFlashAttribute(Constant.MESSAGE, "最低存入100元！");
                    return "redirect:/";
                }
                savings.setType(Constant.SAVE_TWO);
                savings.setV(Constant.V_TWO);
                savings.setYear(1);
            }
            break;
            case Constant.SAVE_TWO_ONE_YEAR_TRUE: {
                if (Double.parseDouble(money) < 100) {
                    redirectAttributes.addFlashAttribute(Constant.MESSAGE, "最低存入100元！");
                    return "redirect:/";
                }
                savings.setType(Constant.SAVE_TWO);
                savings.setV(Constant.V_TWO);
                savings.setYear(2);
            }
            break;
            case Constant.SAVE_TWO_FIVE_YEAR_FALSE: {
                if (Double.parseDouble(money) < 100) {
                    redirectAttributes.addFlashAttribute(Constant.MESSAGE, "最低存入100元！");
                    return "redirect:/";
                }
                savings.setType(Constant.SAVE_TWO);
                savings.setV(Constant.V_TWO);
                savings.setYear(5);
            }
            break;
            case Constant.SAVE_TWO_FIVE_YEAR_TRUE: {
                if (Double.parseDouble(money) < 100) {
                    redirectAttributes.addFlashAttribute(Constant.MESSAGE, "最低存入100元！");
                    return "redirect:/";
                }
                savings.setType(Constant.SAVE_TWO);
                savings.setV(Constant.V_TWO);
                savings.setYear(6);
            }
            break;
            case Constant.SAVE_THREE: {
                if (Double.parseDouble(money) < 50) {
                    redirectAttributes.addFlashAttribute(Constant.MESSAGE, "最低存入50元！");
                    return "redirect:/";
                }
                savings.setV(Constant.V_TWO);
                savings.setType(Constant.SAVE_THREE);
                break;
            }
            default:
                break;
        }

        //交易记录
        Record record = new Record();
        record.setType(Constant.SAVE);
        record.setDate(new Date());
        record.setMoneyStart(new BigDecimal(0));
        record.setMoneyEnd(new BigDecimal(money));
        record.setMoneyType(mType);
        record.setAccount(account);

        //存入数据库
        savings.setId(IDUtil.getId(savingRepository, type.substring(0, 4), mType));
        savings.setAccount(account);
        savings.setDate(new Date());
        savings.setMoney(new BigDecimal(money));
        savings.setLeftMoney(new BigDecimal(0));
        savings.setFlag(true);
        savings.setEnable(true);
        savings.setMoneyType(mType);

        //这里设置所属储蓄账户
        record.setS(savingRepository.save(savings));
        recordRepository.save(record);


        redirectAttributes.addFlashAttribute(Constant.MESSAGE, "成功存入账户: " + account.getName() + " " + money + " 元");
        return "redirect:/";
    }


    @PostMapping("/get")
    public String get(@RequestParam String id, @RequestParam String money, @RequestParam String password,
                      HttpSession session, final RedirectAttributes redirectAttributes) {

        Account account = (Account) session.getAttribute("CURRENT_ACCOUNT");

        //未拿到当前账户，跳转到登陆页面
        if (account == null)
            return "redirect:/login";

        if (!account.getPassword().equals(password)) {
            redirectAttributes.addFlashAttribute(Constant.MESSAGE, "密码不正确！");
            return "redirect:/";
        }

        //拿到对应存款记录
        Savings savings = savingRepository.findRecordById(Long.valueOf(id.split(":")[0]));

        //要取的钱,元以下不计息
        BigDecimal decimal = new BigDecimal(money).setScale(0, BigDecimal.ROUND_DOWN);

        if (savings.getMoney().compareTo(decimal) < 0) {
            redirectAttributes.addFlashAttribute(Constant.MESSAGE, "超出了余额！");
            return "redirect:/";
        }

        Record record = new Record();
        record.setDate(new Date());
        record.setType(Constant.GET);
        record.setMoneyType(savings.getMoneyType());
        record.setMoneyStart(savings.getMoney());
        record.setAccount(account);
        switch (savings.getType()) {
            case Constant.SAVE_ONE: {
                //全部取出
                if (decimal.compareTo(savings.getMoney()) == 0) {
                    //此储蓄子账户不可用
                    savings.setMoney(new BigDecimal(0));
                    savings.setEnable(false);
                    record.setS(savingRepository.save(savings));

                    //计算一年利息
                    BigDecimal res = decimal.multiply(savings.getV());
                    //先求模，因为每过一年会自动记一次息，只需要计剩下时间产生的利息
                    BigDecimal x = new BigDecimal((double) ((new Date().getTime() - savings.getDate().getTime()) % Constant.RANGE)
                            / (double) Constant.RANGE);
                    //显示消息
                    redirectAttributes.addFlashAttribute(Constant.MESSAGE, "成功取款 "
                            + (decimal.add(res.multiply(x)).add(savings.getLeftMoney())
                            .setScale(2, BigDecimal.ROUND_HALF_UP) + "本金: "
                            + decimal + " 元  利息: " + res.multiply(x).setScale(2, BigDecimal.ROUND_HALF_UP) + " 利息余额: "
                            + savings.getLeftMoney()));
                } else {

                    //实际利息
                    BigDecimal v = new BigDecimal((double) ((new Date().getTime() - savings.getDate().getTime()) % Constant.RANGE)
                            / (double) Constant.RANGE).multiply(decimal.multiply(Constant.V_ONE));

                    //只取整数部分
                    BigDecimal res = v.setScale(0, BigDecimal.ROUND_DOWN);
                    //利息余额
                    BigDecimal left = v.subtract(res).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //显示消息
                    redirectAttributes.addFlashAttribute(Constant.MESSAGE, "成功取款 "
                            + (decimal.add(res)) + "元  本金: "
                            + decimal + " 元  利息: " + res + " 存入利息余额: " + left + " 元");

                    //保存记录
                    savings.setMoney(savings.getMoney().subtract(decimal));
                    savings.setLeftMoney(savings.getLeftMoney().add(left));
                    record.setS(savingRepository.save(savings));
                }

                record.setMoneyEnd(savings.getMoney());
                break;
            }

            case Constant.SAVE_TWO: {

                /*
                 * 实际上整存整取取款时的利率总是活期利率：
                 * 因为每过一年或五年利率都会自动计算并入本金
                 * 无论续存与否都相当于部分或全部支取
                 */

                if (!savings.isFlag()) {
                    redirectAttributes.addFlashAttribute(Constant.MESSAGE, "整存整取只能在中途支取一次！");
                    return "redirect:/";
                }

                BigDecimal res;
                //一年存期
                if (savings.getYear() == 1 || savings.getYear() == 2)
                    //先求模，因为每过一年会自动记一次息，只需要计剩下时间产生的利息
                    res = new BigDecimal((double) ((new Date().getTime() - savings.getDate().getTime()) % Constant.RANGE)
                            / (double) Constant.RANGE).multiply(decimal).multiply(Constant.V_ONE).setScale(2, BigDecimal.ROUND_HALF_UP);
                else
                    //先求模，因为每过五年会自动记一次息，只需要计剩下时间产生的利息
                    res = new BigDecimal((double) ((new Date().getTime() - savings.getDate().getTime()) % (Constant.RANGE * 5))
                            / (double) Constant.RANGE).multiply(decimal).multiply(Constant.V_ONE).setScale(2, BigDecimal.ROUND_HALF_UP);
                redirectAttributes.addFlashAttribute(Constant.MESSAGE, "成功取款 "
                        + (decimal.add(res)) + "元  本金: "
                        + decimal + " 元  利息: " + res);
                //全部取出删除此储蓄账户
                if (savings.getMoney().compareTo(decimal) == 0) {
                    savings.setMoney(new BigDecimal(0));
                    savings.setEnable(false);
                    record.setS(savingRepository.save(savings));
                } else {
                    savings.setMoney(savings.getMoney().subtract(decimal));
                    //无法再取款
                    savings.setFlag(false);
                    record.setS(savingRepository.save(savings));
                }
                record.setMoneyEnd(savings.getMoney());
                break;
            }

            case Constant.SAVE_THREE: {
                //不是全部取出
                if (decimal.compareTo(savings.getMoney()) != 0) {
                    redirectAttributes.addFlashAttribute(Constant.MESSAGE, "定活两便存款只能一次性取出！");
                    return "redirect:/";
                } else {
                    //实际利息
                    BigDecimal res = new BigDecimal((double) (new Date().getTime() - savings.getDate().getTime())
                            / (double) Constant.RANGE).multiply(decimal.multiply(Constant.V_TWO))
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                    //超过一年
                    if (new Date().getTime() - savings.getDate().getTime() > Constant.RANGE)
                        redirectAttributes.addFlashAttribute(Constant.MESSAGE, "成功取款 "
                                + (decimal.add(res)) + "元  本金: "
                                + decimal + " 元  利息: " + res);
                    else {
                        //打6折
                        res = res.multiply(new BigDecimal("0.6")).setScale(2, BigDecimal.ROUND_HALF_UP);
                        redirectAttributes.addFlashAttribute(Constant.MESSAGE, "成功取款 "
                                + (decimal.add(res)) + "元  本金: "
                                + decimal + " 元  利息: " + res);
                    }
                    savings.setMoney(new BigDecimal(0));
                    savings.setEnable(false);
                    record.setS(savingRepository.save(savings));
                }
                record.setMoneyEnd(savings.getMoney());
                break;
            }
        }

        recordRepository.save(record);
        return "redirect:/";
    }


}
