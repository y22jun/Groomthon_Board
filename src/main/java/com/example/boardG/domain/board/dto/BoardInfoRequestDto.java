package com.example.boardG.domain.board.dto;

import lombok.Builder;

@Builder
public record BoardInfoRequestDto(Long memberId, String title, String content) {
}
