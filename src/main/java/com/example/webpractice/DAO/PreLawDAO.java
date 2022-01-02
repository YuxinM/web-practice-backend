package com.example.webpractice.DAO;

import com.example.webpractice.po.PreLaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2022/1/2 19:52
 * Interface
 */

@Repository
public interface PreLawDAO extends JpaRepository<PreLaw,Integer> {

    @Query(nativeQuery = true,value = "select * from practice.pre where title=?1")
    List<PreLaw>findByTitle(String title);

}
