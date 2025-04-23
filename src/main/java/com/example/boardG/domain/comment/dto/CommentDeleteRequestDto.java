package com.example.boardG.domain.comment.dto;

import lombok.Builder;

@Builder
public record CommentDeleteRequestDto(

        Long memberId

) {
}
