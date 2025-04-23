package com.example.boardG.domain.comment.dto;

import lombok.Builder;

@Builder
public record CommentInfoResponseDto(
        Long boardId,
        Long memberId,
        String content
) {
}
