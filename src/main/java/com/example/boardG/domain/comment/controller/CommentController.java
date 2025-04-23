package com.example.boardG.domain.comment.controller;

import com.example.boardG.domain.comment.dto.CommentDeleteRequestDto;
import com.example.boardG.domain.comment.dto.CommentSaveRequestDto;
import com.example.boardG.domain.comment.dto.CommentUpdateRequestDto;
import com.example.boardG.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api/v1/comments/{boardId}")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> saveComment(@PathVariable Long boardId, @RequestBody CommentSaveRequestDto commentSaveRequestDto) {
        commentService.saveComment(boardId, commentSaveRequestDto);
        return ResponseEntity.ok("댓글 저장 성공");
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @PathVariable Long boardId, @RequestBody CommentUpdateRequestDto commentUpdateRequestDto) {
        commentService.updateComment(commentId, boardId, commentUpdateRequestDto);
        return ResponseEntity.ok("댓글 수정 성공");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @PathVariable Long boardId, @RequestBody CommentDeleteRequestDto commentDeleteRequestDto) {
        commentService.deleteComment(commentId, boardId, commentDeleteRequestDto);
        return ResponseEntity.ok("댓글 삭제 성공");
    }
}
