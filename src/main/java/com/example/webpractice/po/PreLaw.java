package com.example.webpractice.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author MengYuxin
 * @Date 2022/1/2 19:53
 */

@Entity
@Table(name = "pre")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreLaw {

    public PreLaw(String title, String pre) {
        this.title = title;
        this.pre = pre;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "pre")
    private String pre;
}
