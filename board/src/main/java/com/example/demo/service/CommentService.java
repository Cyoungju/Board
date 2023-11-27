package com.example.demo.service;


import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Comment save(CommentDTO commentDTO) {

        Optional<Board> optionalBoard = boardRepository.findById(commentDTO.getBoardId());


        if(optionalBoard.isPresent()){
            Board board = optionalBoard.get();
            return commentRepository.save(commentDTO.toEntity(board));
        }else{
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다: " + commentDTO.getBoardId());
        }

    }


    // 주어진 게시글 ID를 기반으로 댓글 목록 조회, 조회된 댓글을 commentDTO로 변환후 리스트로 반환
    public List<CommentDTO> findAll(Long boardId) {
        // boardRepository를 사용해 boardId에 해당하는 게시글 찾기
        Board boardEntity = boardRepository.findById(boardId).get();

        // commentRepository를 사용 헤당 게시글에 속한 댓글 목록을 조회
        // 댓글은 ID를 기준으로 내림차순으로 정렬
        List<Comment> commentEntityList = commentRepository.findAllByBoardOrderByIdDesc(boardEntity);

        // 조회된 댓글 목록을 commentDTO로 변환하여 저장할 리스트 생성
        /* EntityList -> DTOList */
        List<CommentDTO> commentDTOList = new ArrayList<>();

        //조회된 댓글 목록을 for문을 돌면서 commentDTO로 변환하고 리스트에 추가
        for (Comment commentEntity: commentEntityList) {
            CommentDTO commentDTO = CommentDTO.tocommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }

        // commentDTO로 변환된 댓글 목록을 반환
        return commentDTOList;
    }


}
