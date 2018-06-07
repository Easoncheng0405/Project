package com.jlu.chengjie.model.form;
/*
 *个人博客:http://www.chengjie-jlu.com
 *Github:https://github.com/Easoncheng0405
 *作者 程杰
 *创建时间 2018/6/4
 */

import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.model.Savings;

import java.util.Date;


/**
 * 表格渲染  bank.html
 */
public class FormSavings {

    private long id;

    private Date date;

    private String mType;

    private String money;

    private String v;

    private String type;

    private String year;

    private String left;

    private String flag;

    private String enable;

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

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public static FormSavings getForm(Savings s){
        FormSavings form = new FormSavings();
        form.setId(s.getId());
        form.setDate(s.getDate());
        form.setmType(s.getMoneyType());
        form.setMoney(s.getMoney().toString());
        form.setV(s.getV().toString());
        form.setType(s.getType());
        if (s.getType().equals(Constant.SAVE_TWO))
            if (s.getYear() == 1 || s.getYear() == 2)
                form.setYear("一年");
            else
                form.setYear("五年");
        else
            form.setYear("不可用");

        form.setLeft(s.getLeftMoney().toString());
        if (s.getType().equals(Constant.SAVE_TWO))
            if (s.isFlag())
                form.setFlag("续存");
            else
                form.setFlag("不续存");
        else
            form.setFlag("不可用");
        if (s.isEnable())
            form.setEnable("可用");
        else
            form.setEnable("失效");

        return form;

    }
}
