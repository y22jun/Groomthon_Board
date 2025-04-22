package com.example.boardG.domain.comment.service;

import com.example.boardG.domain.board.entity.Board;
import com.example.boardG.domain.board.repository.BoardRepository;
import com.example.boardG.domain.comment.dto.CommentSaveRequestDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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