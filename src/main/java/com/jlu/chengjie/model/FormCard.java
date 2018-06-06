package com.jlu.chengjie.model;
/*
 *个人博客:http://www.chengjie-jlu.com
 *Github:https://github.com/Easoncheng0405
 *作者 程杰
 *创建时间 2018/6/6
 */

import java.util.Date;

/**
 * 用于渲染表格
 */
public class FormCard {


    private long id;

    private Date date;

    private String total;

    private String enable;

    //本月待还
    private String borrow;

    //最低还款
    private String least;

    //利息
    private String v;


    //上月未还
    private String last;

    //滞纳金
    private String late;

    //信用卡状态
    private String state;

    //多少个月未还清最低
    private  int month;

    public FormCard(long id, Date date, String total, String enable, String borrow, String least, String v, String last, String late, String state, int month) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.enable = enable;
        this.borrow = borrow;
        this.least = least;
        this.v = v;
        this.last = last;
        this.late = late;
        this.state = state;
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getBorrow() {
        return borrow;
    }

    public void setBorrow(String borrow) {
        this.borrow = borrow;
    }

    public String getLeast() {
        return least;
    }

    public void setLeast(String least) {
        this.least = least;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
