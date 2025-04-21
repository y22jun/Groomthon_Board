package com.example.boardG.domain.board.service;

import com.example.boardG.domain.board.dto.BoardInfoRequestDto;
import com.example.boardG.domain.board.dto.BoardSaveRequestDto;
import com.example.boardG.domain.board.entity.Board;
import com.example.boardG.domain.board.repository.BoardRepository;
import com.example.boardG.domain.member.entity.Member;
import com.example.boardG.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
    }

    @DisplayName("새로운 게시글을 생성한다.")
    @Test
    void createBoard() {
        Member member = getNewMember();
        memberRepository.save(member);

        BoardSaveRequestDto boardSaveRequestDto = BoardSaveRequestDto.builder()
                .memberId(member.getId())
                .title("Test Title")
                .content("Test Content")
                .build();

        boardService.createBoard(member.getId(), boardSaveRequestDto);

        List<Board> boards = boardRepository.findAll();
        assertThat(boards.get(0).getTitle()).isEqualTo("Test Title");
        assertThat(boards.get(0).getContent()).isEqualTo("Test Content");
        assertThat(boards.get(0).getMember().getId()).isEqualTo(member.getId());
    }

    @DisplayName("특정 게시글을 boardId로 조회한다.")
    @Test
    void getBoardInfo() {
        Member member = getNewMember();
        memberRepository.save(member);

        BoardSaveRequestDto boardSaveRequestDto = BoardSaveRequestDto.builder()
                .memberId(member.getId())
                .title("Test Title")
                .content("Test Content")
                .build();

        boardService.createBoard(member.getId(), boardSaveRequestDto);

        Board savedBoard = boardRepository.findAll().get(0);

        BoardInfoRequestDto boardInfoRequestDto = boardService.getBoardInfo(savedBoard.getId());

        assertThat(boardInfoRequestDto.title()).isEqualTo("Test Title");
        assertThat(boardInfoRequestDto.content()).isEqualTo("Test Content");
        assertThat(boardInfoRequestDto.memberId()).isEqualTo(member.getId());
    }

    static Member getNewMember() {
        return Member.builder()
                .username("Test")
                .password("Test")
                .email("Test@test.com")
                .build();
    }

}