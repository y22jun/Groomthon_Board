package com.example.boardG.domain.member.service;

import com.example.boardG.domain.member.dto.MemberUpdateRequestDto;
import com.example.boardG.domain.member.dto.SignInRequestDto;
import com.example.boardG.domain.member.dto.SignUpRequestDto;
import com.example.boardG.domain.member.entity.Member;
import com.example.boardG.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public void signUp(SignUpRequestDto signUpRequestDto) {

        boolean isEmailDuplicate = memberRepository.existsByEmail(signUpRequestDto.email());

        if (isEmailDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member member = Member.builder()
                .username(signUpRequestDto.username())
                .password(signUpRequestDto.password())
                .email(signUpRequestDto.email())
                .build();

        memberRepository.save(member);

    }

    public String signIn(SignInRequestDto signInRequestDto) {

        boolean existsMember = memberRepository.existsByEmail(signInRequestDto.email());

        if (!existsMember) {
            throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
        }

        boolean checkPassword = memberRepository.existsByPassword(signInRequestDto.password());

        if (!checkPassword) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        return "로그인 성공";

    }

    @Transactional
    public void update(Long memberId, MemberUpdateRequestDto memberUpdateRequestDto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        member.updateMember(memberUpdateRequestDto.username(),
                memberUpdateRequestDto.password(),
                memberUpdateRequestDto.email());
    }

}
