package com.example.webpractice.DAO;

import com.example.webpractice.po.Papers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/25 11:51
 * Interface
 */

@Repository
public interface PaperDAO extends JpaRepository<Papers, Integer>, JpaSpecificationExecutor<Papers> {


    List<Papers> getPapersById(int id);

    @Query(nativeQuery = true,value = "select * from practice.papers where title=?1")
    List<Papers>findByTitle(String title);

    @Query(nativeQuery = true,value = "select count(*) from practice.papers where content=?1")
    int numOfFileName(String name);

    @Query(nativeQuery = true,value = "select count(*) from practice.papers where title=?1")
    int numOfTitle(String title);

   // Page<Papers>findAll(Specification<Papers>specification, Pageable pageable);

   // List<Papers>findAll(Specification<Papers>specification);


}
