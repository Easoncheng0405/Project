package com.jlu.chengjie.repository;

import com.jlu.chengjie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/30
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByPidAndPassword(long pid,String password);

    User findUserByPid(long pid);


}
