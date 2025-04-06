package com.example.boardG.domain.board.dto;

import lombok.Builder;

@Builder
public record BoardSaveRequestDto(Long memberId, String title, String content) {
}
