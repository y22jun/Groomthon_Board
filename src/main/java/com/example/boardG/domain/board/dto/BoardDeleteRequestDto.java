package com.example.boardG.domain.board.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BoardDeleteRequestDto(

        @NotNull
        Long memberId

) {
}
