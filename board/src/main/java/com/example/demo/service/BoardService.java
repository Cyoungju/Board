package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository repository;

    public void save(BoardDTO boardDTO){
        boardDTO.setCreateTime(LocalDateTime.now());//현재 시간 넣기
        repository.save(boardDTO.toEntity());
    }

    public void findAll(){
        repository.findAll();
    }


    // ** 페이징을 위한 함수
    // Page<BoardDTO> 페이징을 표현할 리스트
    // pageable 수량 정보 작성
    public Page<BoardDTO> paging(Pageable pageable){
        int page = pageable.getPageNumber() - 1; // 페이지가 1부터 시작하기 (페이지 시작 번호 셋팅)
        int size = 5; // 몇개의 게시물을 넣을것인가 (페이지에 포함될 개수)

        // ** 전체 게시물을 불러온다
        Page<Board> boards = repository.findAll(
            // 정렬시켜서 사가져옴
            PageRequest.of(page, size)
        );

        return boards.map(board -> new BoardDTO( //람다식
                board.getId(),
                board.getUserName(),
                board.getTitle(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime()
                // for문 이라고 생각하면됨
                // 람다식 공부하기!
        ));
    }
}
