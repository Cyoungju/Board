package com.example.demo.controller;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.BoardFile;
import com.example.demo.home.HomeController;
import com.example.demo.home.HomeService;
import com.example.demo.repository.FileRepository;
import com.example.demo.service.BoardService;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
 
@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardservice;
    private final CommentService commentService;
    private final FileRepository fileRepository;
    private final HomeService homeService;

    //Create
    @GetMapping("/create")
    public String create(Model model, HttpServletRequest request){
        homeService.getJwt(model, request);
        return "create"; //templates에 create.html (타임리프)로 이동
    }


    //Read - select
    @GetMapping(value = {"/paging", "/"})
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){
    // @PageableDefault(page = 1) Pageable pageable - 페이지의 기본값으로 1을 가짐
        Page<BoardDTO> boards = boardservice.paging(pageable);
        //페이지 반환

        int blockLimit = 3;
        //한 페이지에 보여질 페이지수
        int startPage = (int)(Math.ceil((double)pageable.getPageNumber() / blockLimit) - 1) * blockLimit + 1;
        // 현재 페이지 블록의 시작 페이지를 계산
        int endPage = ((startPage + blockLimit - 1) < boards.getTotalPages()) ? (startPage + blockLimit - 1) : boards.getTotalPages();
        // 현재 페이지의 블록의 끝페이지를 계산

        model.addAttribute("boardList", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "paging";
    }
    //클라이언트에서 서버 데이터를 사용하기 위해  model.addAttribute 작성


    //id에 해당하는 (바꾸려고 하는) 데이터를 불러와야함
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model, HttpServletRequest request){
        // 해당하는 id값으로 boardDTO가져오기 - Model에 데이터 사용하기위해서
        BoardDTO boardDTO = boardservice.findById(id);
        model.addAttribute("board", boardDTO);//html에 있는 이름 그대로 사용

        // 해당하는 게시물에 있는 파일들 가져오기
        List<BoardFile> existingFiles = fileRepository.findByBoardId(id);
        model.addAttribute("files", existingFiles);
        homeService.getJwt(model, request);

        return "update";
    }
    //get은받아올수 없으니까!
    //get으로 보내서 update에서 post로 변경된 데이터를 받아와야함
    //Model로 보내줘야 html에서 데이터 사용할수 있음


    //Update
    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, MultipartFile[] files) throws IOException {
        //update작업 진행
        boardservice.update(boardDTO, files);

        // 업데이트가완료되면 리스트페이지로 이동
        return "redirect:/board/";
    }
    //@ModelAttribute 데이터 받아와야함(html에서 수정하려고 받은 데이터 받아오기)


    //하나만 불러오게 - 상세페이지
    @GetMapping("/{id}")
    public String paging(@PathVariable Long id, Model model, @PageableDefault(page = 1) Pageable pageable, HttpServletRequest request){
        //게시글 정보 가져오기
        BoardDTO dto = boardservice.findById(id);
        
        //댓글 정보 가져오기
        List<CommentDTO> commentList = commentService.findAll(id);

        //모델에 데이터 추가
        model.addAttribute("board", dto); //html에 있는 변수명이랑 동일하게 사용
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("commentList", commentList);

        List<BoardFile> byBoardFiles = fileRepository.findByBoardId(id);
        model.addAttribute("files", byBoardFiles);

        homeService.getJwt(model, request);

        return "detail";
    }



    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO , @RequestParam MultipartFile[] files) throws IOException {
        //@RequestParam MultipartFile[] files -여러개의 파일을 담기위한 배열

        // 데이터 받아와야함 -> DTO
        // 이미지 저장시도 똑같음 - 파일 불러오기
        boardservice.save(boardDTO , files);

        return "redirect:/board/";
    }


    //Delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardservice.delete(id);
        return "redirect:/board/paging";
    }


    @PostMapping("/checkPassword/{id}")
    @ResponseBody
    public ResponseEntity<String> checkPassword(@PathVariable Long id, @RequestParam String password) {
        try {
            boardservice.checkPassword(id, password);
            return ResponseEntity.ok("Password check successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}















