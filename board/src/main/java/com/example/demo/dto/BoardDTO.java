package com.example.demo.dto;

import com.example.demo.entity.Board;
import lombok.*;

import java.time.LocalDateTime;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    //출력하고 싶은 데이터가 DTO에 없으면 못 불러옴

    private Long id; //정렬을 위해서 가지고옴

    private String userName;

    private String title;

    private String contents;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    @Builder
    public Board toEntity() {
        return Board.builder()
                .title(title)
                .contents(contents)
                .createTime(createTime)
                .updateTime(LocalDateTime.now()) //수정된 시간
                .userName(userName)
                .build();
    }
}
