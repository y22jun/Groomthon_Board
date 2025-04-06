package com.example.boardG.domain.member.dto;

import lombok.Builder;

@Builder
public record SignUpRequestDto(
        String username,
        String password,
        String email
) {
}
