package com.jlu.chengjie.repository;

import com.jlu.chengjie.model.Account;
import com.jlu.chengjie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/30
 */
public interface AccountRepository  extends JpaRepository<Account,Long> {


    Account findByNumber(long number);

    List<Account> findByUser(User user);
}
