package com.example.demo.dto;

import com.example.demo.entity.Board;
import com.example.demo.entity.File;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class FileDTO {

    //파일 경로
    private String filePath;

    //파일이름
    private String fileName;

    //파일 포멧
    private String fileType;

    //파일 크기
    private Long fileSize;

    private Long boardId;


    public File toEntity(Board board){
        return File.builder()
            .filePath(filePath)
            .fileName(fileName)
            .fileType(fileType)
            .fileSize(fileSize)
            .board(board)
            .build();
    }

}














