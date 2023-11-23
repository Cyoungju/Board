package com.example.demo.entity;
import com.example.demo.dto.BoardDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Board {
    //Entity -> DB에 저장하기 위해서
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
    private LocalDateTime createTime;

    // ** 최근 수정 시간
    private LocalDateTime updateTime;

    @Builder
    public Board(Long id, String userName, String title, String contents, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.contents = contents;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void updateFromDTO(BoardDTO boardDTO){
        // 모든 변경 사항을 셋팅. =>  기존에 있는 데이터에 저장해야하기 때문에 new 객체 생성을 하는 toEntity 사용 불가
        this.userName = boardDTO.getUserName();
        this.title = boardDTO.getTitle();
        this.contents = boardDTO.getContents();

    }
}






















