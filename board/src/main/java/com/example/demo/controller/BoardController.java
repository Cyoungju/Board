package com.example.demo.controller;

import com.example.demo.dto.BoardDTO;
import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService service;

    @GetMapping("/")
    public String home(){
        return "createBoard"; //templates 에 createBoard.html (타임리프)
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO){
        //데이터 받아와야함 -> DTO
        System.out.println(boardDTO.getTitle() + ":" + boardDTO.getContents());

        service.save(boardDTO);
        return "";
    }
}
