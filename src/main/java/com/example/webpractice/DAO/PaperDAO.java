package com.example.webpractice.DAO;

import com.example.webpractice.po.Papers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/25 11:51
 * Interface
 */

@Repository

public interface PaperDAO extends JpaRepository<Papers, Integer>, JpaSpecificationExecutor<Papers> {


    List<Papers> getPapersById(int id);

    @Query(nativeQuery = true, value = "select * from practice.papers where title=?1")
    List<Papers> findByTitle(String title);

    @Query(nativeQuery = true, value = "select count(*) from practice.papers where content=?1")
    int numOfFileName(String name);

    @Query(nativeQuery = true, value = "select count(*) from practice.papers where title=?1 and user_id=?2")
    int numOfTitle(String title, int user_id);

    @Query(nativeQuery = true, value = "select count(*) from practice.papers where id=?1")
    int numOfId(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update practice.papers set status=?1 where id=?2")
    void updateStatus(int status, int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update practice.papers set title=?1,number=?2," +
            "category=?3,department=?4,release_time=?5,implement_time=?6,grade=?7," +
            "interpret=?8,user_id=?9,input_time=?10,status=?11 where id=?12")
    void updateWithNoFile(String title, String number, String category,
                          String department, Timestamp release_time,
                          Timestamp implement_time, String grade, String interpret,
                          int user_id, Timestamp input_time, int status, int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update practice.papers set title=?1,number=?2," +
            "category=?3,department=?4,release_time=?5,implement_time=?6,grade=?7," +
            "interpret=?8,user_id=?9,input_time=?10,content=?11,status=?12 where id=?13")
    void updateWithFile(String title, String number, String category,
                        String department, Timestamp release_time,
                        Timestamp implement_time, String grade, String interpret,
                        int user_id, Timestamp input_time, String content, int status, int id);

    @Query(nativeQuery = true, value = "select content from practice.papers where id=?1")
    String findContentById(int id);

    Papers findPapersById(int id);

    void deleteById(int id);


}
