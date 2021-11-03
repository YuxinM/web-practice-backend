package com.example.webpractice.DAO;

import com.example.webpractice.po.Analyse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Repository
public interface AnalyseDAO extends JpaRepository<Analyse, Integer>, JpaSpecificationExecutor<Analyse> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update practice.analyse set title=?1,number=?2,category=?3," +
            "interpret=?4,user_id=?5,input_time=?6 where id=?7")
    void updateWithNoFile(String title, String number, String category, String interpret,
                          int user_id, Timestamp input_time, int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update practice.analyse set title=?1,number=?2,category=?3," +
            "interpret=?4,user_id=?5,input_time=?6,content=?7 where id=?8")
    void updateWithFile(String title, String number, String category, String interpret,
                        int user_id, Timestamp input_time, String content, int id);


}
