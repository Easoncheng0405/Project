package com.jlu.chengjie.model;
/*
 *个人博客:http://www.chengjie-jlu.com
 *Github:https://github.com/Easoncheng0405
 *作者 程杰
 *创建时间 2018/6/5
 */

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 信用卡类
 */
@Entity
public class Card {


    /**
     * 卡号
     */
    @Id
    private long id;

    /**
     * 所属一卡通
     */
    @ManyToOne
    private Account account;


    /**
     * 总额度
     */
    private BigDecimal money;

    /**
     * 密码
     */
    private String password;


    /**
     * 状态
     */
    private String type;


    /**
     * 开卡时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;



    /**
     * 欠款
     */
    private BigDecimal borrow;


    /**
     * 上月未还最低部分
     */
    private BigDecimal clast;


    /**
     * 预借现金手续费
     */
    private BigDecimal temp;


    /**
     * 滞纳金
     */
    private BigDecimal clate;

    /**
     * 用于记录已经产生的利息
     */
    private BigDecimal v;

    /**
     * 用于记录多个月未达到最低还款
     */
    private int cindex;


    /**
     * 用于标记本月是否已达到最低还款
     */
    private int tag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getBorrow() {
        return borrow;
    }

    public void setBorrow(BigDecimal borrow) {
        this.borrow = borrow;
    }

    public BigDecimal getClast() {
        return clast;
    }

    public void setClast(BigDecimal clast) {
        this.clast = clast;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getTemp() {
        return temp;
    }

    public void setTemp(BigDecimal temp) {
        this.temp = temp;
    }

    public BigDecimal getClate() {
        return clate;
    }

    public void setClate(BigDecimal clate) {
        this.clate = clate;
    }

    public int getCindex() {
        return cindex;
    }

    public void setCindex(int cindex) {
        this.cindex = cindex;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public BigDecimal getV() {
        return v;
    }

    public void setV(BigDecimal v) {
        this.v = v;
    }
}
