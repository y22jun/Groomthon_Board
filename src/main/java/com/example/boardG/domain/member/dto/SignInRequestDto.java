package com.example.boardG.domain.member.dto;

import lombok.Builder;

@Builder
public record SignInRequestDto(
        String email,
        String password
) {
}
