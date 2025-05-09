package com.example.boardG.domain.member.service;

import com.example.boardG.domain.member.dto.MemberUpdateRequestDto;
import com.example.boardG.domain.member.dto.SignInRequestDto;
import com.example.boardG.domain.member.dto.SignUpRequestDto;
import com.example.boardG.domain.member.entity.Member;
import com.example.boardG.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("사용자로부터 받은 정보를 통해 회원가입을 진행한다.")
    @Test
    void signUp() {
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .username("Test")
                .password("Test")
                .email("Test@test.com")
                .build();

        memberService.signUp(signUpRequestDto);

        Optional<Member> member = memberRepository.findByEmail("Test@test.com");

        assertThat(member).isPresent();
        assertThat(member.get().getUsername()).isEqualTo(signUpRequestDto.username());
    }

    @DisplayName("이미 존재하는 이메일로 회원 가입을 시도하면 예외가 발생한다.")
    @Test
    void signUpThrowExceptionCausedByDuplicateEmail() throws Exception {

        Member member = Member.builder()
                .username("Test")
                .password("Test")
                .email("Test@test.com")
                .build();

        memberRepository.save(member);

        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .username("Test")
                .password("Test")
                .email("Test@test.com")
                .build();

        assertThatThrownBy(() -> memberService.signUp(signUpRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일입니다.");

    }

    @DisplayName("사용자로부터 정보를 입력받아 로그인을 진행한다.")
    @Test
    void signIn() {
        Member member = Member.builder()
                .username("Test")
                .password("Test")
                .email("Test@test.com")
                .build();

        memberRepository.save(member);

        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .email("Test@test.com")
                .password("Test")
                .build();

        String result = memberService.signIn(signInRequestDto);

        assertThat(result).isEqualTo("로그인 성공");

    }

    @DisplayName("존재하지 않는 이메일을 받아오면 예외가 발생한다.")
    @Test
    void signInThrowExceptionCausedByDuplicateEmail() {

        Member member = Member.builder()
                .username("Test")
                .password("Test")
                .email("Test@test.com")
                .build();

        memberRepository.save(member);

        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .email("Test1234@test.com")
                .password("Test")
                .build();

        assertThatThrownBy(() -> memberService.signIn(signInRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 이메일입니다.");

    }

    @DisplayName("비밀번호가 틀리면 예외가 발생한다.")
    @Test
    void signInThrowExceptionCausedByDuplicatePassword() {

        Member member = Member.builder()
                .username("Test")
                .password("Test")
                .email("Test@test.com")
                .build();

        memberRepository.save(member);

        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .email("Test@test.com")
                .password("Test1234")
                .build();

        assertThatThrownBy(() -> memberService.signIn(signInRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 틀렸습니다.");
    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        Member member = Member.builder()
                .username("Test")
                .password("Test")
                .email("Test@test.com")
                .build();

        memberRepository.save(member);

        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .username("UpdatedUser")
                .password("updatedPass")
                .email("updated@email.com")
                .build();

        memberService.update(member.getId(), memberUpdateRequestDto);

        assertThat(memberUpdateRequestDto.username()).isEqualTo("UpdatedUser");
        assertThat(memberUpdateRequestDto.password()).isEqualTo("updatedPass");
        assertThat(memberUpdateRequestDto.email()).isEqualTo("updated@email.com");
    }

    @DisplayName("존재하지 않는 회원 정보를 수정하려고 하면 예외가 발생한다.")
    @Test
    void updateMemberThrowExceptionCausedByDuplicateId() {
        Member member = Member.builder()
                .username("Test")
                .password("Test")
                .email("Test@test.com")
                .build();
        memberRepository.save(member);

        Long invalidMemberId = 999L;

        MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                .username("UpdatedUser")
                .password("updatedPass")
                .email("updated@email.com")
                .build();

        assertThatThrownBy(() -> memberService.update(invalidMemberId, memberUpdateRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }

    @DisplayName("회원 탈퇴를 진행한다.")
    @Test
    void deleteMember() {
        Member member = Member.builder()
                .username("Test")
                .password("Test")
                .email("Test@test.com")
                .build();

        memberRepository.save(member);

        Long memberId = member.getId();

        memberService.delete(memberId);

        Optional<Member> deletedMember = memberRepository.findById(memberId);
        assertThat(deletedMember).isNotPresent();
    }

    @DisplayName("존재하지 않는 회원을 탈퇴하려고 하면 예외가 발생한다.")
    @Test
    void deleteMemberThrowExceptionCausedByDuplicateId() {
        Member member = Member.builder()
                .username("Test")
                .password("Test")
                .email("Test@test.com")
                .build();
        memberRepository.save(member);

        Long invalidMemberId = 999L;

        assertThatThrownBy(() -> memberService.delete(invalidMemberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }

}