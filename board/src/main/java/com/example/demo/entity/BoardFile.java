package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class BoardFile {

    //@Column 컬럼의 속성을 지정할때만 사용해주면됨

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //파일 경로
    private String filePath;

    // ** uuid 랜덤키
    private String uuid;

    //파일이름
    private String fileName;

    //파일 포멧
    private String fileType;

    //파일 크기
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    @Builder
    public BoardFile(Long id, String filePath, String fileName, String uuid, String fileType, Long fileSize, Board board) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
        this.uuid = uuid;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.board = board;
    }


}












