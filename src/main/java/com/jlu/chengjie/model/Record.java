package com.jlu.chengjie.model;
/*
 *个人博客:http://www.chengjie-jlu.com
 *Github:https://github.com/Easoncheng0405
 *作者 程杰
 *创建时间 2018/6/4
 */


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Record {


    /**
     * 交易流水号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    /**
     * 交易前余额
     */
    private BigDecimal moneyStart;


    /**
     * 交易后余额
     */
    private BigDecimal moneyEnd;


    /**
     * 币种
     */
    private String moneyType;


    /**
     * 交易时间
     */
    private Date date;


    /**
     * 交易类型
     */
    private String type;

    /**
     * 所属储蓄账户
     */
    @ManyToOne
    private Savings s;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public BigDecimal getMoneyStart() {
        return moneyStart;
    }

    public void setMoneyStart(BigDecimal moneyStart) {
        this.moneyStart = moneyStart;
    }

    public BigDecimal getMoneyEnd() {
        return moneyEnd;
    }

    public void setMoneyEnd(BigDecimal moneyEnd) {
        this.moneyEnd = moneyEnd;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
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

    public Savings getS() {
        return s;
    }

    public void setS(Savings s) {
        this.s = s;
    }
}
