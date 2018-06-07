package com.jlu.chengjie.repository;
/*
 *个人博客:http://www.chengjie-jlu.com
 *Github:https://github.com/Easoncheng0405
 *作者 程杰
 *创建时间 2018/6/7
 */


import com.jlu.chengjie.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlanRepository extends JpaRepository<Plan,Long> {

    Plan findById(long id);

    Plan findByType(String type);
}
