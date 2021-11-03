package com.example.webpractice.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author PanYue
 * @Date 2021/11/3 14:16
 * 每一条内化法律文书的详细信息
 */


@Entity
@Table(name = "analyse")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Analyse {

    public Analyse(String title, String paper_number, String category, String interpret_department, int user_id, Timestamp input_time, String content, int paper_id) {
        this.title = title;
        this.paper_number = paper_number;
        this.category = category;
        this.interpret_department = interpret_department;
        this.user_id = user_id;
        this.input_time = input_time;
        this.content = content;
        this.paper_id = paper_id;
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

    @Column(name = "interpret")
    private String interpret_department;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "input_time")
    private Timestamp input_time;

    @Column(name = "content")
    private String content;

    @Column(name = "paper_id")
    private int paper_id;


}
