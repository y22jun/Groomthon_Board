package com.example.boardG.domain.member.dto;

import lombok.Builder;

@Builder
public record MemberUpdateRequestDto(String username, String password, String email) {
}
