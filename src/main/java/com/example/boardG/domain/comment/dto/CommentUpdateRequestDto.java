package com.example.boardG.domain.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CommentUpdateRequestDto(

        Long memberId,

        @NotNull(message = "내용을 입력해주세요.")
        String content

) {
}
