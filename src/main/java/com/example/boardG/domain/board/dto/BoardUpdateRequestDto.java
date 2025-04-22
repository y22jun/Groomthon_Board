package com.example.boardG.domain.board.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BoardUpdateRequestDto(

        /*현재 jwt 로그인 방식이 아닌 단순 로그인 방식을 사용하므로 memberId를 가지고 오는 방법이 없다고 판단을 함.
        세션도 사용을 안 하므로 일단 멤버 아이디를 입력하는 방식으로 구현을 할 예정.
         */
        @NotNull(message = "멤버 아이디를 입력해주세요.")
        Long memberId,

        @NotNull(message = "제목을 입력해주세요.")
        String title,

        @NotNull(message = "내용을 입력해주세요.")
        String content

) {
}
