package com.example.demo.controller;

import com.example.demo.dto.BoardDTO;
import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@RequiredArgsConstructor
@Controller
//@RequestMapping("/board")
public class BoardController {

    private final BoardService service;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/createBoard")
    public String create(){
        return "createBoard"; //templates에 createBoard.html (타임리프)
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO){
        //데이터 받아와야함 -> DTO
        System.out.println(boardDTO.getTitle() + ":" + boardDTO.getContents());

        service.save(boardDTO);
        return "redirect:";
    }

    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){
        Page<BoardDTO> boards = service.paging(pageable);

        int blockLimit = 3;
        int startPage = (int)(Math.ceil((double)pageable.getPageNumber() / blockLimit) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boards.getTotalPages()) ? (startPage + blockLimit - 1) : boards.getTotalPages();


        model.addAttribute("boardList", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "paging";
    }
}
