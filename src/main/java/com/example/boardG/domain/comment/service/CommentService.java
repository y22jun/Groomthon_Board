package com.example.boardG.domain.comment.service;

import com.example.boardG.domain.board.entity.Board;
import com.example.boardG.domain.board.repository.BoardRepository;
import com.example.boardG.domain.comment.dto.CommentSaveRequestDto;
import com.example.boardG.domain.comment.entity.Comment;
import com.example.boardG.domain.comment.repository.CommentRepository;
import com.example.boardG.domain.member.entity.Member;
import com.example.boardG.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
