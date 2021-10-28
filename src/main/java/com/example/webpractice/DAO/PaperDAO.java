package com.example.webpractice.DAO;

import com.example.webpractice.po.Papers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/25 11:51
 * Interface
 */

@Repository
public interface PaperDAO extends JpaRepository<Papers,Integer> {


    List<Papers>getPapersById(int id);



}
