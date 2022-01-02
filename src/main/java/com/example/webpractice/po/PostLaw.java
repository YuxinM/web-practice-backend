package com.example.webpractice.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author MengYuxin
 * @Date 2022/1/2 19:57
 */

@Entity
@Table(name = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostLaw {

    public PostLaw(String title, String post) {
        this.title = title;
        this.post = post;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "post")
    private String post;
}
