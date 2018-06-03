package com.jlu.chengjie.runner;

import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.model.Savings;
import com.jlu.chengjie.repository.SavingRepository;
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

    @Autowired
    public TimeRunner(SavingRepository savingRepository) {
        this.savingRepository = savingRepository;
    }

    /**
     * 活期
     */
    @Scheduled(fixedRate = Constant.RANGE)
    public void run1() {

        System.out.println("run1");

        //先找到所有储蓄子账户
        List<Savings> savings = savingRepository.findByType(Constant.SAVE_ONE);

        //利滚利
        for (Savings s : savings) {

            //存够一年
            long range = new Date().getTime() - s.getDate().getTime();
            if (range > Constant.RANGE)
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_ONE)));
            else
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_ONE).multiply(new
                        BigDecimal((double) range / (double) Constant.RANGE))).setScale(2, BigDecimal.ROUND_DOWN));
            savingRepository.save(s);
        }
    }


    /**
     * 定活两便
     */
    @Scheduled(fixedRate = Constant.RANGE)
    public void run2() {

        System.out.println("run2");
        //先找到所有储蓄子账户
        List<Savings> savings = savingRepository.findByType(Constant.SAVE_THREE);

        //利滚利
        for (Savings s : savings) {

            //存够一年
            long range = new Date().getTime() - s.getDate().getTime();
            if (range > Constant.RANGE)
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO)));
            else
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO).multiply(new
                        BigDecimal((double) range / (double) Constant.RANGE))).setScale(2, BigDecimal.ROUND_DOWN));
            savingRepository.save(s);
        }
    }


    /**
     * 一年不续存
     */
    @Scheduled(fixedRate = Constant.RANGE)
    public void run3() {
        System.out.println("run3");

        //先找到所有储蓄子账户
        List<Savings> savings = savingRepository.findByYear(1);

        //利滚利
        for (Savings s : savings) {
            long range = new Date().getTime() - s.getDate().getTime();
            if (range > Constant.RANGE) {
                //新的活期
                Savings one = new Savings();

                one.setId(savingRepository.findAll().size() + Constant.RMB_ONE);
                one.setV(Constant.V_ONE);
                one.setType(Constant.SAVE_ONE);
                one.setAccount(s.getAccount());
                one.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO)));
                one.setDate(new Date());
                one.setLeftMoney(new BigDecimal(0));
                //保存新的活期
                savingRepository.save(one);
                //删除旧的定期
                savingRepository.delete(s);
            }
            //走过一个完整的计息时段
            else {
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO).multiply(new
                        BigDecimal((double) range / (double) Constant.RANGE))).setScale(2, BigDecimal.ROUND_DOWN));
                savingRepository.save(s);
            }

        }
    }


    /**
     * 五年不续存
     */
    @Scheduled(fixedRate = Constant.RANGE * 5)
    public void run4() {
        System.out.println("run4");
        //先找到所有储蓄子账户
        List<Savings> savings = savingRepository.findByYear(5);

        //利滚利
        for (Savings s : savings) {
            long range = new Date().getTime() - s.getDate().getTime();
            if (range > Constant.RANGE * 5) {
                //新的活期
                Savings one = new Savings();

                one.setId(savingRepository.findAll().size() + Constant.RMB_ONE);
                one.setV(Constant.V_ONE);
                one.setType(Constant.SAVE_ONE);
                one.setAccount(s.getAccount());
                one.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO).multiply(new BigDecimal("5"))));
                one.setDate(new Date());
                one.setLeftMoney(new BigDecimal(0));
                //保存新的活期
                savingRepository.save(one);
                //删除旧的定期
                savingRepository.delete(s);
            } else {
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO).
                        multiply(new BigDecimal((double) range / (double) Constant.RANGE)))
                        .setScale(2, BigDecimal.ROUND_DOWN));

                savingRepository.save(s);
            }


        }

    }


    /**
     * 一年续存
     */
    @Scheduled(fixedRate = Constant.RANGE)
    public void run5() {
        System.out.println("run5");
        //先找到所有储蓄子账户
        List<Savings> savings = savingRepository.findByYear(2);
        for (Savings s : savings) {

            long range = new Date().getTime() - s.getDate().getTime();
            if (range > Constant.RANGE)
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO)));
            else
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO).multiply(new
                        BigDecimal((double) range / (double) Constant.RANGE))).setScale(2, BigDecimal.ROUND_DOWN));
            //再次可取款
            s.setFlag(true);

            savingRepository.save(s);
        }
    }


    /**
     * 五年续存
     */
    @Scheduled(fixedRate = Constant.RANGE * 5)
    public void run6() {
        System.out.println("run6");
        //先找到所有储蓄子账户
        List<Savings> savings = savingRepository.findByYear(6);
        for (Savings s : savings) {

            long range = new Date().getTime() - s.getDate().getTime();
            if (range > Constant.RANGE)
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO)).multiply(new BigDecimal("5")));
            else
                s.setMoney(s.getMoney().add(s.getMoney().multiply(Constant.V_TWO).
                        multiply(new BigDecimal((double) range / (double) Constant.RANGE)))
                        .setScale(2, BigDecimal.ROUND_DOWN));
            //再次可取款
            s.setFlag(true);

            savingRepository.save(s);
        }
    }

}
