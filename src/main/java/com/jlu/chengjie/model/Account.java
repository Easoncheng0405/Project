package com.jlu.chengjie.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/28
 */

@Entity
public class Account {

    /**
     * 账户卡号 5位数字
     */
    @Id
    private long number;

    /**
     * 账户名称，有别于用户名称
     */
    private String name;

    /**
     * 金额
     */
    private double money;

    /**
     * 利率
     */
    private double v;

    /**
     * 存入时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    /**
     * 账户状态
     */
    private String state;


    private String password;

    @ManyToOne
    private User user;


    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
