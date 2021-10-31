package com.example.webpractice.DAO;

import com.example.webpractice.blImpl.LibraryCreateServiceImpl;
import com.example.webpractice.po.Appendix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/29 19:22
 * Interface
 */

@Repository
public interface AppendixDAO extends JpaRepository<Appendix,Integer> {


    @Query(nativeQuery = true,value = "select * from practice.appendix where file_name=?1")
    List<Appendix>findByFileName(String fileName);

    @Query(nativeQuery = true,value = "select * from practice.appendix where paper_id=?1")
    List<Appendix>findByPaperId(int paper_id);

    void deleteById(int id);

    Appendix findAppendixById(int id);






}
