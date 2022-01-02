package com.example.webpractice.DAO;

import com.example.webpractice.po.AbolishLaw;
import com.example.webpractice.po.PreLaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2022/1/2 20:00
 * Interface
 */

@Repository
public interface AbolishLawDAO extends JpaRepository<AbolishLaw,Integer> {

    @Query(nativeQuery = true,value = "select * from practice.abolish where title=?1")
    List<AbolishLaw> findByTitle(String title);
}
