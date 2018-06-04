package com.jlu.chengjie.repository;

import com.jlu.chengjie.model.Account;
import com.jlu.chengjie.model.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/6/3
 */

@Repository
public interface SavingRepository extends JpaRepository<Savings, Long> {

    List<Savings> findByTypeAndEnable(String type,boolean enable);

    List<Savings> findByEnableAndAccount(boolean enable, Account account);

    Savings findRecordById(long id);

    List<Savings> findByYearAndEnable(int year,boolean enable);


    List<Savings> findByTypeAndMoneyType(String type,String moneyType);

}
