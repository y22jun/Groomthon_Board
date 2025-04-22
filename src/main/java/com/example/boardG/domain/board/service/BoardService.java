package com.example.boardG.domain.board.service;

import com.example.boardG.domain.board.dto.BoardInfoRequestDto;
import com.example.boardG.domain.board.dto.BoardSaveRequestDto;
import com.example.boardG.domain.board.dto.BoardUpdateRequestDto;
import com.example.boardG.domain.board.entity.Board;
import com.example.boardG.domain.board.repository.BoardRepository;
import com.example.boardG.domain.member.entity.Member;
import com.example.boardG.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public void createBoard(Long memberId, BoardSaveRequestDto boardSaveRequestDto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원압니다."));

        Board board = Board.builder()
                .member(member)
                .title(boardSaveRequestDto.title())
                .content(boardSaveRequestDto.content())
                .build();

        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public BoardInfoRequestDto getBoardInfo(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다"));

        return BoardInfoRequestDto.builder()
                .memberId(board.getMember().getId())
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }

    @Transactional
    public void updateBoard(Long memberId, Long boardId, BoardUpdateRequestDto boardUpdateRequestDto) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다"));

        board.updateBoard(boardUpdateRequestDto.title(), boardUpdateRequestDto.content());

    }

}
