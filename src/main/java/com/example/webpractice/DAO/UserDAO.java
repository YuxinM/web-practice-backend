package com.example.webpractice.DAO;

import com.example.webpractice.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @Author MengYuxin
 * @Date 2021/10/25 11:52
 * Interface
 */

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {


    @Query(nativeQuery = true, value = "select username from practice.user where id=?1")
    String getUsernameById(int id);

    @Query(nativeQuery = true, value = "select count(*) from practice.user where username=?1")
    int UserExists(String username);

    @Query(nativeQuery = true, value = "select count(*) from practice.user where id=?1")
    int UserExistsById(int id);

    @Query(nativeQuery = true, value = "select * from practice.user where username=?1")
    User getLoginInfo(String name);


}
