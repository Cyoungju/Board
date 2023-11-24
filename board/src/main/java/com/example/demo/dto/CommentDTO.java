package com.example.demo.dto;


import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;

    private String writer;

    private String contents;

    private Long boardId;

    public Comment toEntity(Board board) {
        return Comment.builder()
                .writer(writer)
                .contents(contents)
                .board(board)
                .build();
    }

    public static CommentDTO tocommentDTO(Comment comment,Long boardId){
        return new CommentDTO(
                comment.getId(),
                comment.getWriter(),
                comment.getContents(),
                boardId
                );
    }


}
