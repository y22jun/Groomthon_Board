package com.example.boardG.domain.board.service;

import com.example.boardG.domain.board.dto.BoardInfoRequestDto;
import com.example.boardG.domain.board.dto.BoardSaveRequestDto;
import com.example.boardG.domain.board.dto.BoardUpdateRequestDto;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
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

    @DisplayName("특정 게시글을 수정한다.")
    @Test
    void updateBoard() {
        Member member = getNewMember();
        memberRepository.save(member);

        BoardSaveRequestDto boardSaveRequestDto = BoardSaveRequestDto.builder()
                .memberId(member.getId())
                .title("Original Title")
                .content("Original Content")
                .build();

        boardService.createBoard(member.getId(), boardSaveRequestDto);
        Board savedBoard = boardRepository.findAll().get(0);

        BoardUpdateRequestDto updateRequestDto = BoardUpdateRequestDto.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        boardService.updateBoard(member.getId(), savedBoard.getId(), updateRequestDto);

        Board updatedBoard = boardRepository.findById(savedBoard.getId()).orElseThrow();
        assertThat(updatedBoard.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedBoard.getContent()).isEqualTo("Updated Content");
    }

    @DisplayName("존재하지 않는 회원으로 게시글을 수정하려고 할 때 예외가 발생한다.")
    @Test
    void updateBoardMemberIdThrowExceptionCausedByDuplicateId() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        BoardUpdateRequestDto updateDto = BoardUpdateRequestDto.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        Long invalidMemberId = 999L;

        assertThatThrownBy(() -> boardService.updateBoard(invalidMemberId, board.getId(), updateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }

    @DisplayName("존재하지 않는 게시글을 삭제하려고 할 때 예외가 발생한다.")
    @Test
    void updateBoardThrowExceptionCausedByDuplicateId() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        BoardUpdateRequestDto updateDto = BoardUpdateRequestDto.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        Long invalidBoardID = 999L;

        assertThatThrownBy(() -> boardService.updateBoard(member.getId(), invalidBoardID, updateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 게시글을 찾을 수 없습니다.");
    }

    @DisplayName("특정 게시글을 삭제한다.")
    @Test
    void deleteBoard() {
        Member member = getNewMember();
        memberRepository.save(member);

        BoardSaveRequestDto boardSaveRequestDto = BoardSaveRequestDto.builder()
                .memberId(member.getId())
                .title("Original Title")
                .content("Original Content")
                .build();

        boardService.createBoard(member.getId(), boardSaveRequestDto);
        Board savedBoard = boardRepository.findAll().get(0);

        boardService.deleteBoard(member.getId(), savedBoard.getId());

        assertThat(boardRepository.findById(savedBoard.getId())).isEmpty();
    }

    @DisplayName("존재하지 않는 회원으로 게시글을 삭제하려고 할 때 예외가 발생한다.")
    @Test
    void deleteBoardMemberIdThrowExceptionCausedByDuplicateId() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        Long invalidMemberId = 999L;

        assertThatThrownBy(() -> boardService.deleteBoard(invalidMemberId, board.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }

    @DisplayName("존재하지 않는 게시글을 삭제하려고 할 때 예외가 발생한다.")
    @Test
    void deleteBoardThrowExceptionCausedByDuplicateId() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        Long invalidBoardID = 999L;

        assertThatThrownBy(() -> boardService.deleteBoard(member.getId(), invalidBoardID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 게시글을 찾을 수 없습니다.");
    }

    @DisplayName("모든 게시글을 조회한다.")
    @Test
    void getAllBoards() {
        Member member = getNewMember();
        memberRepository.save(member);

        BoardSaveRequestDto boardSaveRequestDto1 = BoardSaveRequestDto.builder()
                .memberId(member.getId())
                .title("Test Title1")
                .content("Test Content1")
                .build();
        boardService.createBoard(member.getId(), boardSaveRequestDto1);

        BoardSaveRequestDto boardSaveRequestDto2 = BoardSaveRequestDto.builder()
                .memberId(member.getId())
                .title("Test Title2")
                .content("Test Content2")
                .build();
        boardService.createBoard(member.getId(), boardSaveRequestDto2);

        List<Board> boards = boardRepository.findAll();
        assertThat(boards).hasSize(2);

        assertThat(boards.get(0).getTitle()).isEqualTo("Test Title1");
        assertThat(boards.get(0).getContent()).isEqualTo("Test Content1");

        assertThat(boards.get(1).getTitle()).isEqualTo("Test Title2");
        assertThat(boards.get(1).getContent()).isEqualTo("Test Content2");
    }

    static Member getNewMember() {
        return Member.builder()
                .username("Test")
                .password("Test")
                .email("Test@test.com")
                .build();
    }

    static Board getNewBoard(Member member) {
        return Board.builder()
                .title("Test Title")
                .content("Test Content")
                .member(member)
                .build();
    }

}