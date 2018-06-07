package com.jlu.chengjie.runner;

import com.jlu.chengjie.model.*;
import com.jlu.chengjie.repository.CardRepository;
import com.jlu.chengjie.repository.PlanRepository;
import com.jlu.chengjie.repository.RecordRepository;
import com.jlu.chengjie.repository.SavingRepository;
import com.jlu.chengjie.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/*
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/6/3
 */


/**
 * 系统启动时会设定若干个计息点
 * <p>
 * 整存整取不续存账户在经过计息点时
 * <p>
 * 若未达到储蓄年限，则仅计算利息并计入本金
 * <p>
 * 经过一个完整计息时段才会转为活期
 */
@Component
public class TimeRunner {

    private final SavingRepository savingRepository;

    private final RecordRepository recordRepository;

    private final CardRepository cardRepository;

    private final PlanRepository planRepository;

    @Autowired
    public TimeRunner(SavingRepository savingRepository, RecordRepository recordRepository
            , CardRepository cardRepository, PlanRepository planRepository) {
        this.savingRepository = savingRepository;
        this.recordRepository = recordRepository;
        this.cardRepository = cardRepository;
        this.planRepository = planRepository;
    }

    /**
     * 活期
     */
    @Scheduled(fixedRate = Constant.RANGE)
    public void run1() {


        //先找到所有储蓄子账户
        List<Savings> savings = savingRepository.findByTypeAndEnable(Constant.SAVE_ONE, true);

        //利滚利
        for (Savings s : savings) {

            //如果此储蓄账户不可用，直接跳过
            if (!s.isEnable())
                continue;

            //交易记录
            Record record = new Record();
            record.setDate(new Date());
            record.setMoneyStart(s.getMoney());
            record.setMoneyType(s.getMoneyType());
            record.setType(Constant.AUTO);
            record.setAccount(s.getAccount());
            //存够一年
            long range = new Date().getTime() - s.getDate().getTime();
            if (range > Constant.RANGE)
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_ONE)));
                //未存够一年
            else
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_ONE).multiply(new
                        BigDecimal((double) range / (double) Constant.RANGE))).setScale(2, BigDecimal.ROUND_DOWN));

            record.setMoneyEnd(s.getMoney());

            //存到数据库
            record.setS(savingRepository.save(s));
            recordRepository.save(record);

            System.out.println(1);
        }
    }


    /**
     * 定活两便
     */
    @Scheduled(fixedRate = Constant.RANGE)
    public void run2() {

        //先找到所有储蓄子账户
        List<Savings> savings = savingRepository.findByTypeAndEnable(Constant.SAVE_TWO, true);

        //利滚利
        for (Savings s : savings) {

            //如果此储蓄账户不可用，直接跳过
            if (!s.isEnable())
                continue;

            //交易记录
            Record record = new Record();
            record.setDate(new Date());
            record.setMoneyStart(s.getMoney());
            record.setMoneyType(s.getMoneyType());
            record.setType(Constant.AUTO);
            record.setAccount(s.getAccount());

            //存够一年
            long range = new Date().getTime() - s.getDate().getTime();
            if (range > Constant.RANGE)
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO)));
            else
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO).multiply(new
                        BigDecimal((double) range / (double) Constant.RANGE))).setScale(2, BigDecimal.ROUND_DOWN));


            record.setMoneyEnd(s.getMoney());

            //存到数据库
            record.setS(savingRepository.save(s));
            recordRepository.save(record);
            System.out.println(2);
        }
    }


    /**
     * 一年不续存
     */
    @Scheduled(fixedRate = Constant.RANGE)
    public void run3() {

        //先找到所有储蓄子账户
        List<Savings> savings = savingRepository.findByYearAndEnable(1, true);

        //利滚利
        for (Savings s : savings) {

            //如果此储蓄账户不可用，直接跳过
            if (!s.isEnable())
                continue;

            //交易记录
            Record record = new Record();
            record.setDate(new Date());
            record.setMoneyStart(s.getMoney());
            record.setMoneyType(s.getMoneyType());
            record.setAccount(s.getAccount());


            long range = new Date().getTime() - s.getDate().getTime();
            if (range > Constant.RANGE) {
                //新的活期
                Savings one = new Savings();

                one.setId(IDUtil.getId(savingRepository, s.getType(), s.getMoneyType()));
                one.setV(Constant.V_ONE);
                one.setType(Constant.SAVE_ONE);
                one.setAccount(s.getAccount());
                one.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO)));
                one.setDate(new Date());
                one.setLeftMoney(new BigDecimal(0));
                one.setMoneyType(s.getMoneyType());
                one.setEnable(true);
                //保存新的活期
                savingRepository.save(one);
                //旧的定期不可用
                s.setEnable(false);

                record.setMoneyEnd(s.getMoney());
                record.setType(Constant.AUTO);
                //存到数据库
                record.setS(savingRepository.save(s));
                recordRepository.save(record);
            }
            //走过一个完整的计息时段
            else {
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO).multiply(new
                        BigDecimal((double) range / (double) Constant.RANGE))).setScale(2, BigDecimal.ROUND_DOWN));

                record.setMoneyEnd(s.getMoney());
                record.setType(Constant.CHANGE);
                //存到数据库
                record.setS(savingRepository.save(s));
                recordRepository.save(record);
            }
            System.out.println(3);
        }
    }


    /**
     * 五年不续存
     */
    @Scheduled(fixedRate = Constant.RANGE * 5)
    public void run4() {

        //先找到所有储蓄子账户
        List<Savings> savings = savingRepository.findByYearAndEnable(5, true);

        //利滚利
        for (Savings s : savings) {

            //如果此储蓄账户不可用，直接跳过
            if (!s.isEnable())
                continue;

            //交易记录
            Record record = new Record();
            record.setDate(new Date());
            record.setMoneyStart(s.getMoney());
            record.setMoneyType(s.getMoneyType());
            record.setAccount(s.getAccount());


            long range = new Date().getTime() - s.getDate().getTime();
            if (range > Constant.RANGE * 5) {
                //新的活期
                Savings one = new Savings();

                one.setId(IDUtil.getId(savingRepository, s.getType(), s.getMoneyType()));
                one.setV(Constant.V_ONE);
                one.setType(Constant.SAVE_ONE);
                one.setAccount(s.getAccount());
                one.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO).multiply(new BigDecimal("5"))));
                one.setDate(new Date());
                one.setLeftMoney(new BigDecimal(0));
                one.setMoneyType(s.getMoneyType());
                one.setEnable(true);
                //保存新的活期
                savingRepository.save(one);
                //旧的定期不可用
                s.setEnable(false);

                record.setMoneyEnd(s.getMoney());
                record.setType(Constant.AUTO);
                //存到数据库
                record.setS(savingRepository.save(s));
                recordRepository.save(record);

            }

            //必须存够一个完整的计息段才会转存
            else {
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO).
                        multiply(new BigDecimal((double) range / (double) Constant.RANGE)))
                        .setScale(2, BigDecimal.ROUND_DOWN));

                record.setType(Constant.CHANGE);
                record.setMoneyEnd(s.getMoney());

                //存到数据库
                record.setS(savingRepository.save(s));
                recordRepository.save(record);
            }

            System.out.println(4);
        }


    }


    /**
     * 一年续存
     */
    @Scheduled(fixedRate = Constant.RANGE)
    public void run5() {

        //先找到所有储蓄子账户
        List<Savings> savings = savingRepository.findByYearAndEnable(2, true);
        for (Savings s : savings) {

            //如果此储蓄账户不可用，直接跳过
            if (!s.isEnable())
                continue;

            //交易记录
            Record record = new Record();
            record.setDate(new Date());
            record.setMoneyStart(s.getMoney());
            record.setMoneyType(s.getMoneyType());
            record.setType(Constant.CONTINUE);
            record.setAccount(s.getAccount());


            long range = new Date().getTime() - s.getDate().getTime();
            if (range > Constant.RANGE)
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO)));
            else
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO).multiply(new
                        BigDecimal((double) range / (double) Constant.RANGE))).setScale(2, BigDecimal.ROUND_DOWN));
            //再次可取款
            s.setFlag(true);

            record.setMoneyEnd(s.getMoney());

            //存到数据库
            record.setS(savingRepository.save(s));
            recordRepository.save(record);
            System.out.println(5);
        }
    }


    /**
     * 五年续存
     */
    @Scheduled(fixedRate = Constant.RANGE * 5)
    public void run6() {

        //先找到所有储蓄子账户
        List<Savings> savings = savingRepository.findByYearAndEnable(6, true);
        for (Savings s : savings) {

            //如果此储蓄账户不可用，直接跳过
            if (!s.isEnable())
                continue;

            //交易记录
            Record record = new Record();
            record.setDate(new Date());
            record.setMoneyStart(s.getMoney());
            record.setMoneyType(s.getMoneyType());
            record.setType(Constant.CONTINUE);
            record.setAccount(s.getAccount());


            long range = new Date().getTime() - s.getDate().getTime();
            if (range > Constant.RANGE)
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO)).multiply(new BigDecimal("5")));
            else
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO).
                        multiply(new BigDecimal((double) range / (double) Constant.RANGE)))
                        .setScale(2, BigDecimal.ROUND_DOWN));
            //再次可取款
            s.setFlag(true);

            record.setMoneyEnd(s.getMoney());

            //存到数据库
            record.setS(savingRepository.save(s));
            recordRepository.save(record);
            System.out.println(6);
        }
    }

    /**
     * 每过10s清查一次信用卡
     */
    @Scheduled(fixedRate = Constant.RANGE)
    public void run7() {


        //先找信用卡
        List<Card> cards = cardRepository.findAll();

        //获取现在的时间
        Date date = new Date();

        for (Card c : cards) {

            //没欠钱已注销，跳过
            if (c.getBorrow().compareTo(new BigDecimal(0)) < 1 || c.getType().equals("已注销"))
                continue;

            //无论卡还款或没还款都要对当前的欠款进行计息
            //下月20才开始清算，不到时间的跳过
            if (date.getTime() - c.getDate().getTime() > Constant.RANGE) {


                //先计息
                c.setV(c.getBorrow().multiply(Constant.V_CARD));

                //处理是否达到最低还款
                if (c.getTag() == 1) {

                    //违规月数加1
                    c.setCindex(c.getCindex() + 1);

                    //计入百分之五的滞纳金
                    c.setClate(c.getClast().multiply(new BigDecimal("0.05")));
                    if (c.getCindex() == 3)
                        c.setType("冻结");//冻结次卡
                    if (c.getCindex() == 4)
                        c.setType("已注销"); //销毁此卡
                }


                //经过这个月后所有还有欠款的信用卡再次标记为待还款
                if (c.getBorrow().compareTo(new BigDecimal(0)) > 0)
                    c.setTag(1);
                cardRepository.save(c);
            }


        }
    }


    /**
     * 自动计划任务 每过10s检查一次
     */
    @Scheduled(fixedRate = 10000)
    public void run8() {

        Plan plan = planRepository.findByType("就绪");
        if (plan == null || plan.getDate().after(new Date()))
            return;

        Constant.V_ONE = new BigDecimal(plan.getV1()).setScale(4, BigDecimal.ROUND_DOWN);
        Constant.V_TWO = new BigDecimal(plan.getV2()).setScale(4, BigDecimal.ROUND_DOWN);
        Constant.V_CARD = new BigDecimal(plan.getCard()).setScale(4, BigDecimal.ROUND_DOWN);

        plan.setType("已完成");

        System.out.print("已完成");
        planRepository.save(plan);
    }

}
