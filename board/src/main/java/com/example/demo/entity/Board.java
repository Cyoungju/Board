package com.example.demo.entity;
import com.example.demo.dto.BoardDTO;
import com.example.demo.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(length = 100, nullable = false)
    private String title;

    // ** 내용
    @Column(length = 300)
    private String contents;

    // ** 작성 시간
    private LocalDateTime createTime;

    // ** 최근 수정 시간
    private LocalDateTime updateTime;

    private String password;

    private String userEmail;



    // ** 연관관계 매핑 Entity에 작성 - Table
    // ** 1:다 연관관계
    // ** 소유(One)와 비소유(Many) - 비소유에서 확인해야함
    // cascade = CascadeType.REMOVE : 소유한 쪽에서 데이터를 지웠을때(게시글 삭제시), 댓글도 자동으로 삭제
    // orphanRemoval = true : 연결관계가 끊어지는경우 자동으로 삭제
    // fetch = FetchType.LAZY : 지연로딩(성능 최적화)
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comment = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFile> files = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



    @Builder
    public Board(Long id, String userName, String title, String contents, LocalDateTime createTime, LocalDateTime updateTime, String password, String userEmail) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.contents = contents;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.password = password;
        this.userEmail = userEmail;
    }


    public void updateFromDTO(BoardDTO boardDTO){
        // 모든 변경 사항을 셋팅. =>  기존에 있는 데이터에 저장해야하기 때문에 new 객체 생성을 하는 toEntity 사용 불가
        this.userName = boardDTO.getUserName();
        this.title = boardDTO.getTitle();
        this.contents = boardDTO.getContents();
        this.password = boardDTO.getPassword();
    }

}






















