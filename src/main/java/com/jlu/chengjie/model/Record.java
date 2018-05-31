package com.jlu.chengjie.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/31
 */

/**
 * 交易记录表
 */
@Entity
public class Record {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    /**
     * 交易时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    /**
     * 交易类型
     */
    private String type;

    /**
     * 交易金额
     */
    private double money;


    /**
     * 所属账户  ,多条交易记录对应一个账户
     */
    @ManyToOne
    private Account account;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
