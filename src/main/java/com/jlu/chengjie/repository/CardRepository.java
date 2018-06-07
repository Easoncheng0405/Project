package com.jlu.chengjie.repository;
/*
 *个人博客:http://www.chengjie-jlu.com
 *Github:https://github.com/Easoncheng0405
 *作者 程杰
 *创建时间 2018/6/5
 */

import com.jlu.chengjie.model.Account;
import com.jlu.chengjie.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Card findById(long id);

    List<Card> findByAccountAndType(Account account, String type);


    List<Card> findByAccount(Account account);
}
