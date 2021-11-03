package com.example.webpractice.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * @Author MengYuxin
 * @Date 2021/10/24 16:17
 * 每一条法律文书的详细信息
 */


@Entity
@Table(name = "papers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Papers {


    public Papers(String title, String paper_number, String category,
                  String department, Timestamp release_time, Timestamp implement_time,
                  String grade, String interpret_department, int user_id, Timestamp input_time,
                  String content, int status, int analyse_id) {
        this.title = title;
        this.paper_number = paper_number;
        this.category = category;
        this.department = department;
        this.release_time = release_time;
        this.implement_time = implement_time;
        this.grade = grade;
        this.interpret_department = interpret_department;
        this.user_id = user_id;
        this.input_time = input_time;
        this.content = content;
        this.status = status;
        this.analyse_id = analyse_id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "number")
    private String paper_number;

    @Column(name = "category")
    private String category;

    @Column(name = "department")
    private String department;

    @Column(name = "release_time")
    private Timestamp release_time;

    @Column(name = "implement_time")
    private Timestamp implement_time;

    @Column(name = "grade")
    private String grade;

    @Column(name = "interpret")
    private String interpret_department;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "input_time")
    private Timestamp input_time;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private int status;

    @Column(name = "analyse_id")
    private int analyse_id;


}
