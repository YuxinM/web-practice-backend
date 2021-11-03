package com.example.webpractice.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 附件
 *
 * @Author MengYuxin
 * @Date 2021/10/29 19:19
 */


@Entity
@Table(name = "appendix")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appendix {

    public Appendix(String file_name, String user_name, int paper_id, int analyse_paper_id) {
        this.file_name = file_name;
        this.user_name = user_name;
        this.paper_id = paper_id;
        this.analyse_paper_id = analyse_paper_id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "file_name")
    private String file_name;

    @Column(name = "user_name")
    private String user_name;

    @Column(name = "paper_id")
    private int paper_id;

    @Column(name = "analyse_paper_id")
    private int analyse_paper_id;


}
