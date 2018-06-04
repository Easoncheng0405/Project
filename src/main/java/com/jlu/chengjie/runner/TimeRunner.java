package com.jlu.chengjie.runner;

import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.model.Record;
import com.jlu.chengjie.model.Savings;
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

    @Autowired
    public TimeRunner(SavingRepository savingRepository, RecordRepository recordRepository) {
        this.savingRepository = savingRepository;
        this.recordRepository = recordRepository;
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
        }
    }


    /**
     * 定活两便
     */
    @Scheduled(fixedRate = Constant.RANGE)
    public void run2() {

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
        }
    }
}
