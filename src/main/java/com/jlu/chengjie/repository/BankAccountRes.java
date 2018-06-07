package com.jlu.chengjie.repository;
/*
 *个人博客:http://www.chengjie-jlu.com
 *Github:https://github.com/Easoncheng0405
 *作者 程杰
 *创建时间 2018/6/6
 */


import com.jlu.chengjie.model.Account;
import com.jlu.chengjie.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRes extends JpaRepository<BankAccount,Long> {

    BankAccount findByName(String name);

    BankAccount findByAccount(Account account);
}
