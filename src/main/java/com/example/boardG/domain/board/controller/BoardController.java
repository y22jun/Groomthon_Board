package com.example.boardG.domain.board.controller;

import com.example.boardG.domain.board.dto.BoardDeleteRequestDto;
import com.example.boardG.domain.board.dto.BoardInfoRequestDto;
import com.example.boardG.domain.board.dto.BoardSaveRequestDto;
import com.example.boardG.domain.board.dto.BoardUpdateRequestDto;
import com.example.boardG.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
@RestController
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BoardSaveRequestDto boardSaveRequestDto) {
        boardService.createBoard(boardSaveRequestDto.memberId(), boardSaveRequestDto);
        return ResponseEntity.ok("게시판 저장 성공");
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId) {
        BoardInfoRequestDto boardInfoRequestDto = boardService.getBoardInfo(boardId);
        return ResponseEntity.ok(boardInfoRequestDto);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId, @RequestBody BoardUpdateRequestDto boardUpdateRequestDto) {
        boardService.updateBoard(boardUpdateRequestDto.memberId(), boardId, boardUpdateRequestDto);
        return ResponseEntity.ok("게시판 수정 성공");
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId, @RequestBody BoardDeleteRequestDto boardDeleteRequestDto) {
        boardService.deleteBoard(boardDeleteRequestDto.memberId(), boardId);
        return ResponseEntity.ok("게시판 삭제 성공");
    }

}
