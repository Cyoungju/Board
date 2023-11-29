package com.example.demo.controller;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Comment;
import com.example.demo.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO){

        Comment comment = commentService.save(commentDTO);

        //저장된 댓글가져오기
        List<CommentDTO> all = commentService.findAll(commentDTO.getBoardId());

        //예외처리 해주기
        if(comment != null){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }

    }

    /* UPDATE */
    @PostMapping("/update/{id}/comments/{commentId}")
    public ResponseEntity<Long> update(@PathVariable Long id, @PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
        commentService.update(id, commentId, commentDTO);
        return ResponseEntity.ok(id);
    }


    /* GET COMMENT BY ID */
    @GetMapping("/getComment/{id}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable Long id) {
        CommentDTO comment = commentService.getCommentById(id);

        if (comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}/comments/{commentId}")
    public ResponseEntity<Long> delete(@PathVariable Long id, @PathVariable Long commentId) {
        commentService.delete(id, commentId);
        return ResponseEntity.ok(commentId);
    }

}
