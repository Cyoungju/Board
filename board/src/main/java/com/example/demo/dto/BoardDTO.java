package com.example.demo.dto;

import com.example.demo.entity.Board;
import lombok.*;

import javax.persistence.Column;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private String title;

    private String contents;

    @Builder
    public Board toEntity() {
        return Board.builder()
                .title(title)
                .contents(contents)
                .build();
    }
}
