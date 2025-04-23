package com.example.boardG.domain.comment.service;

import com.example.boardG.domain.board.entity.Board;
import com.example.boardG.domain.board.repository.BoardRepository;
import com.example.boardG.domain.comment.dto.CommentDeleteRequestDto;
import com.example.boardG.domain.comment.dto.CommentInfoResponseDto;
import com.example.boardG.domain.comment.dto.CommentSaveRequestDto;
import com.example.boardG.domain.comment.dto.CommentUpdateRequestDto;
import com.example.boardG.domain.comment.entity.Comment;
import com.example.boardG.domain.comment.repository.CommentRepository;
import com.example.boardG.domain.member.entity.Member;
import com.example.boardG.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public void saveComment(Long boardId, CommentSaveRequestDto commentSaveRequestDto) {
        Member member = memberRepository.findById(commentSaveRequestDto.memberId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .member(member)
                .board(board)
                .content(commentSaveRequestDto.content())
                .build();
        commentRepository.save(comment);

    }

    @Transactional
    public void updateComment(Long commentId, Long boardId, CommentUpdateRequestDto commentUpdateRequestDto) {
        memberRepository.findById(commentUpdateRequestDto.memberId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        comment.updateContent(commentUpdateRequestDto.content());
    }

    @Transactional
    public void deleteComment(Long commentId, Long boardId, CommentDeleteRequestDto commentDeleteRequestDto) {
        memberRepository.findById(commentDeleteRequestDto.memberId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentInfoResponseDto> getComments(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);

        return comments.stream()
                .map(comment -> CommentInfoResponseDto.builder()
                        .boardId(comment.getBoard().getId())
                        .memberId(comment.getMember().getId())
                        .content(comment.getContent())
                        .build())
                .toList();
    }
}
