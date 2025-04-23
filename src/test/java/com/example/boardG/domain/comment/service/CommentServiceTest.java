package com.example.boardG.domain.comment.service;

import com.example.boardG.domain.board.entity.Board;
import com.example.boardG.domain.board.repository.BoardRepository;
import com.example.boardG.domain.comment.dto.CommentDeleteRequestDto;
import com.example.boardG.domain.comment.dto.CommentSaveRequestDto;
import com.example.boardG.domain.comment.dto.CommentUpdateRequestDto;
import com.example.boardG.domain.comment.entity.Comment;
import com.example.boardG.domain.comment.repository.CommentRepository;
import com.example.boardG.domain.member.entity.Member;
import com.example.boardG.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("특정 게시글 안 댓글을 저장합니다.")
    @Test
    void createComment() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        CommentSaveRequestDto commentSaveRequestDto = CommentSaveRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content")
                .build();

        commentService.saveComment(board.getId(), commentSaveRequestDto);

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(1);
        Comment savedComment = comments.get(0);
        assertThat(savedComment.getContent()).isEqualTo("Test Comment Content");
        assertThat(savedComment.getMember().getId()).isEqualTo(member.getId());
        assertThat(savedComment.getBoard().getId()).isEqualTo(board.getId());
    }

    @DisplayName("존재하지 않는 회원으로 댓글을 저장하려고 할 때 예외가 발생한다.")
    @Test
    void createCommentWithInvalidMemberId() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        Long invalidMemberId = 999L;
        CommentSaveRequestDto invalidDto = CommentSaveRequestDto.builder()
                .memberId(invalidMemberId)
                .content("Test Comment Content")
                .build();

        assertThatThrownBy(() -> commentService.saveComment(board.getId(), invalidDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원을 찾을 수 없습니다.");
    }

    @DisplayName("존재하지 않는 게시글에 댓글을 저장하려고 할 때 예외가 발생한다.")
    @Test
    void createCommentWithInvalidBoardId() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        CommentSaveRequestDto commentSaveRequestDto = CommentSaveRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content")
                .build();
        commentService.saveComment(board.getId(), commentSaveRequestDto);

        Long invalidBoardId = 999L;

        assertThatThrownBy(() -> commentService.saveComment(invalidBoardId, commentSaveRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 게시글을 찾을 수 없습니다.");

    }

    @DisplayName("특정 게시글 안 특정 댓글을 수정합니다.")
    @Test
    void updateComment() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        CommentSaveRequestDto commentSaveRequestDto = CommentSaveRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content")
                .build();
        commentService.saveComment(board.getId(), commentSaveRequestDto);

        Comment saveComment = commentRepository.findAll().get(0);

        CommentUpdateRequestDto commentUpdateRequestDto = CommentUpdateRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content1")
                .build();

        commentService.updateComment(saveComment.getId(), board.getId(), commentUpdateRequestDto);

        Comment updatedComment = commentRepository.findById(saveComment.getId()).orElseThrow();
        assertThat(updatedComment.getContent()).isEqualTo("Test Comment Content1");
    }

    @DisplayName("존재하지 않는 회원으로 댓글을 수정하려고 할 때 예외가 발생한다.")
    @Test
    void updateCommentWithInvalidMemberId() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        CommentSaveRequestDto commentSaveRequestDto = CommentSaveRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content")
                .build();
        commentService.saveComment(board.getId(), commentSaveRequestDto);

        Comment saveComment = commentRepository.findAll().get(0);

        Long invalidMemberId = 999L;

        CommentUpdateRequestDto commentUpdateRequestDto = CommentUpdateRequestDto.builder()
                .memberId(invalidMemberId)
                .content("Test Comment Content1")
                .build();

        assertThatThrownBy(() -> commentService.updateComment(saveComment.getId(), board.getId(), commentUpdateRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원을 찾을 수 없습니다.");
    }

    @DisplayName("존재하지 않는 게시글에 댓글을 수정하려고 할 때 예외가 발생한다.")
    @Test
    void updateCommentWithInvalidBoardId() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        CommentSaveRequestDto commentSaveRequestDto = CommentSaveRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content")
                .build();
        commentService.saveComment(board.getId(), commentSaveRequestDto);

        Comment saveComment = commentRepository.findAll().get(0);

        Long invalidBoardId = 999L;

        CommentUpdateRequestDto commentUpdateRequestDto = CommentUpdateRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content1")
                .build();

        assertThatThrownBy(() -> commentService.updateComment(saveComment.getId(), invalidBoardId, commentUpdateRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 게시글을 찾을 수 없습니다.");
    }

    @DisplayName("특정 게시글 안에 존재하지 않는 댓글을 수정하려고 할 때 예외가 발생한다.")
    @Test
    void updateCommentWithInvalidCommentId() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        CommentSaveRequestDto commentSaveRequestDto = CommentSaveRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content")
                .build();
        commentService.saveComment(board.getId(), commentSaveRequestDto);

        Long invalidCommentId = 999L;

        CommentUpdateRequestDto commentUpdateRequestDto = CommentUpdateRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content1")
                .build();

        assertThatThrownBy(() -> commentService.updateComment(invalidCommentId, board.getId(), commentUpdateRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 댓글을 찾을 수 없습니다.");
    }

    @DisplayName("특정 게시글 안 특정 댓글을 삭제합니다.")
    @Test
    void deleteComment() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        CommentSaveRequestDto commentSaveRequestDto = CommentSaveRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content")
                .build();
        commentService.saveComment(board.getId(), commentSaveRequestDto);

        Comment saveComment = commentRepository.findAll().get(0);

        CommentDeleteRequestDto commentDeleteRequestDto = CommentDeleteRequestDto.builder()
                .memberId(member.getId())
                .build();

        commentService.deleteComment(saveComment.getId(), board.getId(), commentDeleteRequestDto);

        assertThat(commentRepository.findById(saveComment.getId())).isEmpty();
    }

    @DisplayName("존재하지 않는 회원으로 특정 댓글을 삭제하려고 할 때 예외를 발생한다.")
    @Test
    void deleteCommentWithInvalidMemberId() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        CommentSaveRequestDto commentSaveRequestDto = CommentSaveRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content")
                .build();
        commentService.saveComment(board.getId(), commentSaveRequestDto);

        Comment saveComment = commentRepository.findAll().get(0);

        Long invalidMemberId = 999L;

        CommentDeleteRequestDto commentDeleteRequestDto = CommentDeleteRequestDto.builder()
                .memberId(invalidMemberId)
                .build();

        assertThatThrownBy(() -> commentService.deleteComment(saveComment.getId(), board.getId(), commentDeleteRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원을 찾을 수 없습니다.");
    }

    @DisplayName("존재하지 않는 게시글에 특정 댓글을 삭제하려고 할 때 예외를 발생한다.")
    @Test
    void deleteCommentWithInvalidBoardId() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        CommentSaveRequestDto commentSaveRequestDto = CommentSaveRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content")
                .build();
        commentService.saveComment(board.getId(), commentSaveRequestDto);

        Comment saveComment = commentRepository.findAll().get(0);

        CommentDeleteRequestDto commentDeleteRequestDto = CommentDeleteRequestDto.builder()
                .memberId(member.getId())
                .build();

        Long invalidBoardId = 999L;

        assertThatThrownBy(() -> commentService.deleteComment(saveComment.getId(), invalidBoardId, commentDeleteRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 게시글을 찾을 수 없습니다.");
    }

    @DisplayName("특정 게시글 안에 존재하지 않는 댓글을 삭제하려고 할 때 예외가 발생한다.")
    @Test
    void deleteCommentWithInvalidCommentId() {
        Member member = getNewMember();
        memberRepository.save(member);

        Board board = getNewBoard(member);
        boardRepository.save(board);

        CommentSaveRequestDto commentSaveRequestDto = CommentSaveRequestDto.builder()
                .memberId(member.getId())
                .content("Test Comment Content")
                .build();
        commentService.saveComment(board.getId(), commentSaveRequestDto);

        Long invalidCommentId = 999L;

        CommentDeleteRequestDto commentDeleteRequestDto = CommentDeleteRequestDto.builder()
                .memberId(member.getId())
                .build();

        assertThatThrownBy(() -> commentService.deleteComment(invalidCommentId, board.getId(), commentDeleteRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 댓글을 찾을 수 없습니다.");




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