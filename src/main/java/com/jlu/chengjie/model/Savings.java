package com.jlu.chengjie.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/6/3
 */

/**
 * 储蓄子账户
 */
@Entity
public class Savings {

    /**
     * 储蓄子账户账号 5位数字
     */
    @Id
    private long id;


    /**
     * 金额
     */
    private BigDecimal money;


    /**
     * 存入日利息
     */
    private BigDecimal v;


    /**
     * 存入日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;


    /**
     * 利息余额
     */
    private BigDecimal leftMoney;


    /**
     * 存款类型
     */
    private String type;


    /**
     * 用于标记整存整取是否已取过款
     */
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean flag;


    /**
     * 1 : 1年不续存
     * 2 : 1年续存
     * 5 : 5年不续存
     * 6 : 5年续存
     */
    private int year;

    /**
     * 所属一卡通账户
     * 多一个储蓄子账户对应一给一卡通账户
     */
    @ManyToOne
    private Account account;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getV() {
        return v;
    }

    public void setV(BigDecimal v) {
        this.v = v;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getLeftMoney() {
        return leftMoney;
    }

    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
