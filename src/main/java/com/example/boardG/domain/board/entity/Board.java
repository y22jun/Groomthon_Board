package com.example.boardG.domain.board.entity;

import com.example.boardG.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board")
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    @Builder
    private Board(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
