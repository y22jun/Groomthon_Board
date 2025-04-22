package com.example.boardG.domain.comment.controller;

import com.example.boardG.domain.comment.dto.CommentSaveRequestDto;
import com.example.boardG.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public ResponseEntity<?> saveComment(@PathVariable Long boardId, @RequestBody CommentSaveRequestDto commentSaveRequestDto) {
        commentService.saveComment(boardId, commentSaveRequestDto);
        return ResponseEntity.ok("댓글 저장 성공");
    }
}
