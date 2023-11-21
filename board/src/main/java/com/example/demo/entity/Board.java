package com.example.demo.entity;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
public class Board {
    // ** pk
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ** 작성자 이름
    @Column(length = 50)
    private String userName;

    // ** 게시물 제목
    @Column(length = 50)
    private String title;

    // ** 내용
    @Column(length = 50)
    private String contents;

    // ** 작성 시간
    private LocalTime createTime;

    // ** 최근 수정 시간
    private LocalTime updateTime;

    @Builder
    public Board(Long id, String userName, String title, String contents, LocalTime createTime, LocalTime updateTime) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.contents = contents;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}

