package com.jlu.chengjie.repository;
/*
 *个人博客:http://www.chengjie-jlu.com
 *Github:https://github.com/Easoncheng0405
 *作者 程杰
 *创建时间 2018/6/4
 */


import com.jlu.chengjie.model.Account;
import com.jlu.chengjie.model.Record;
import com.jlu.chengjie.model.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findByAccountAndDateBetween(Account account,Date st, Date ed);

    List<Record> findByS(Savings s);

    List<Record> findByAccount(Account account);
}
