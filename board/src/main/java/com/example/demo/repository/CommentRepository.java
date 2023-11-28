package com.example.demo.repository;

import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardOrderByIdDesc(Board board);

    Optional<Comment> findByIdAndBoard(Long commentId, Board boardEntity);

}
