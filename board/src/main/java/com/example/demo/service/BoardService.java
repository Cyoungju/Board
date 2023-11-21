package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository repository;

    public void save(BoardDTO boardDTO){
        repository.save(boardDTO.toEntity());
    }
}
