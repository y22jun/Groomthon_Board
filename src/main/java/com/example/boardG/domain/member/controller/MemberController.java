package com.example.boardG.domain.member.controller;

import com.example.boardG.domain.member.dto.MemberUpdateRequestDto;
import com.example.boardG.domain.member.dto.SignInRequestDto;
import com.example.boardG.domain.member.dto.SignUpRequestDto;
import com.example.boardG.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        memberService.signUp(signUpRequestDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDto signInRequestDto) {
        return ResponseEntity.ok(memberService.signIn(signInRequestDto));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<?> update(@PathVariable("memberId") Long memberId, @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        memberService.update(memberId, memberUpdateRequestDto);
        return ResponseEntity.ok("회원 수정 성공");
    }

    @DeleteMapping("{memberId}")
    public ResponseEntity<?> delete(@PathVariable("memberId") Long memberId) {
        memberService.delete(memberId);
        return ResponseEntity.ok("회원 삭제 성공");
    }

}
