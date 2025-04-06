package com.example.boardG.domain.board.controller;

import com.example.boardG.domain.board.dto.BoardSaveRequestDto;
import com.example.boardG.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
