package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String writer;

    @Column(length = 300)
    private String contents;


    private LocalDateTime createTime;

    //비소유 했지만 확인 가능
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(Long id, String writer, String contents,LocalDateTime createTime, Board board) {
        this.id = id;
        this.writer = writer;
        this.contents = contents;
        this.createTime = createTime;
        this.board = board;
    }




}