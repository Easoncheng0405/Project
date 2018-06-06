package com.jlu.chengjie.model;

import javax.persistence.*;
import java.util.List;

/*
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/28
 */


/**
 * 一卡通账户
 */
@Entity
public class Account {

    /**
     * 账户帐号 10位数字
     */
    @Id
    private long id;

    /**
     * 账户持有人姓名
     */
    private String name;


    /**
     * 账户持有人身份证号
     */
    private String pid;


    /**
     * 账户持有人住址
     */
    private String address;


    /**
     * 账户持有人联系电话
     */
    private String phone;


    /**
     * 账户状态
     */
    private String state;

    /**
     * 账户6为密码
     */
    private String password;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

}
