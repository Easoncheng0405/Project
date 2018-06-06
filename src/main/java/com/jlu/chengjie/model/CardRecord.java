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
 * 信用卡交易记录
 */
@Entity
public class CardRecord {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * 交易时间
     */
    private Date date;

    /**
     * 交易金额
     */
    private BigDecimal money;


    /**
     * 信用卡剩余可用额度
     */
    private BigDecimal moneyLeft;


    /**
     * 消费类型
     */
    private String type;


    /**
     * 所属信用卡
     */
    @ManyToOne
    private Card card;

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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoneyLeft() {
        return moneyLeft;
    }

    public void setMoneyLeft(BigDecimal moneyLeft) {
        this.moneyLeft = moneyLeft;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
