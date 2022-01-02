package com.example.webpractice.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author MengYuxin
 * @Date 2022/1/2 19:58
 */

@Entity
@Table(name = "abolish")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbolishLaw {

    public AbolishLaw(String title, String abolish) {
        this.title = title;
        this.abolish = abolish;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "abolish")
    private String abolish;
}
