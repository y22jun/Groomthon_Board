package com.example.boardG.domain.comment.entity;

import com.example.boardG.domain.board.entity.Board;
import com.example.boardG.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String content;

    @Builder
    private Comment(Member member, Board board, String content) {
        this.member = member;
        this.board = board;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
