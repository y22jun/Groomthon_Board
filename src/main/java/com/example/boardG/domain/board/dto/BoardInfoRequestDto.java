package com.example.boardG.domain.board.dto;

import com.example.boardG.domain.board.entity.Board;
import lombok.Builder;

@Builder
public record BoardInfoRequestDto(Long memberId, String title, String content) {

    public static BoardInfoRequestDto from(Board board) {
        return new BoardInfoRequestDto(board.getId(), board.getTitle(), board.getContent());
    }
}
