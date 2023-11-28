package com.example.demo.dto;

import com.example.demo.entity.BoardFile;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
// DTO에 있는 필드값이 Entity에서 꼭 사용할 필요는 없음
// 궁극적으로 Entity를 사용하기 위해서
public class FileDTO {

    //파일 경로
    private String filePath;

    //파일이름
    private String fileName;

    // ** uuid (랜덤키)
    private String uuid;

    //파일 포멧
    private String fileType;

    //파일 크기
    private Long fileSize;

    private Long boardId;


    public BoardFile toEntity(){
        return BoardFile.builder()
            .filePath(filePath)
            .fileName(fileName)
            .uuid(uuid)
            .fileType(fileType)
            .fileSize(fileSize)
            .build();
    }

}














