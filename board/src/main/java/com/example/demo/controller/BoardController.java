package com.example.demo.controller;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.FileDTO;
import com.example.demo.service.BoardService;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
/*
 * 최초작성자 : 최영주
 * 최초작성일 : 2023.11.10.
 * 최종변경일 : 2023.11.27.
 * 목적 : 게시판 CRUD 작성
 * 개정이력 : 최영주,2023.11.27,utf-8지원
 * 최영주,2023.11.27,댓글작성 기능 추가
 */


@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardservice;
    private final CommentService commentService;

    //Create
    @GetMapping("/create")
    public String create(){
        return "create"; //templates에 create.html (타임리프)
    }


    //Read - select
    @GetMapping(value = {"/paging", "/"})
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){
        Page<BoardDTO> boards = boardservice.paging(pageable);

        int blockLimit = 3;
        int startPage = (int)(Math.ceil((double)pageable.getPageNumber() / blockLimit) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boards.getTotalPages()) ? (startPage + blockLimit - 1) : boards.getTotalPages();

        model.addAttribute("boardList", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "paging";
    }


    //id에 해당하는 (바꾸려고 하는) 데이터를 불러와야함
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){
        BoardDTO boardDTO = boardservice.findById(id);
        model.addAttribute("board", boardDTO);//html에 있는 이름 그대로 사용
        return "update";
    }
    //get은받아올수 없으니까!
    //get으로 보내서 update에서 post로 변경된 데이터를 받아와야함
    //Model로 보내줘야 html에서 데이터 사용할수 있음

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO){
        boardservice.update(boardDTO);

        return "redirect:/board/";
    }
    //@ModelAttribute 데이터 받아와야함(html에서 수정하려고 받은 데이터 받아오기)


    //하나만 불러오게
    @GetMapping("/{id}")
    public String paging(@PathVariable Long id, Model model, @PageableDefault(page = 1) Pageable pageable){
        //게시글 정보 가져오기
        BoardDTO dto = boardservice.findById(id);
        
        //댓글 정보 가져오기
        List<CommentDTO> commentList = commentService.findAll(id);

        //모델에 데이터 추가
        model.addAttribute("board", dto); //html에 있는 변수명이랑 동일하게 사용
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("commentList", commentList);

        return "detail";
    }


    //Update
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO, FileDTO fileDTO, @RequestParam MultipartFile[] files) throws IOException {
        // 데이터 받아와야함 -> DTO
        // 이미지 저장시도 똑같음 - 파일 불러오기
        //@RequestParam MultipartFile[] file

        boardDTO.setCreateTime(LocalDateTime.now());//현재 시간 넣기
        boardservice.save(boardDTO, fileDTO , files);

        return "redirect:/board/";
    }


    //Delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardservice.delete(id);
        return "redirect:/board/paging";
    }


}















