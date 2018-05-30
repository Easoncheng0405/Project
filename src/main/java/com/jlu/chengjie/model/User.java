package com.jlu.chengjie.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/30
 */

/**
 * 使用银行系统的用户必须创建一个账户
 *
 * 一个用户对应一个一卡通账户
 *
 * 管理员账户不属于此类别,由超级管理员创建
 */
@Entity
public class User {

    /**
     * 10位一卡通号码
     */
    @Id
    private long number;

    /**
     * 身份证号码
     */
    private long pid;


    /**
     * 用户姓名
     */
    private String name;

    /**
     *联系地址
     */
    private String address;


    /**
     * 联系方式
     */
    private String phone;


    /**
     * 用户密码
     */
    private String password;


    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
