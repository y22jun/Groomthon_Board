package com.example.boardG.domain.board.service;

import com.example.boardG.domain.board.dto.BoardSaveRequestDto;
import com.example.boardG.domain.board.entity.Board;
import com.example.boardG.domain.board.repository.BoardRepository;
import com.example.boardG.domain.member.entity.Member;
import com.example.boardG.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
