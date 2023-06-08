package com.ll.FlexGym.domain.Board.entity;

import com.ll.FlexGym.domain.BoardLike.entity.BoardLike;
import com.ll.FlexGym.domain.Comment.entity.Comment;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import com.ll.FlexGym.global.rsData.RsData;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
@Builder
public class Board extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Member member;

    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL})
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL})
    private List<BoardLike> boardLikes = new ArrayList<>();

    private String title;
    private String content;
    private Long views;
    private String category;

    public void updateTitle(String title){
        this.title = title;
    }

    public void updateContent(String content){
        this.content = content;
    }

    public void addToBoardLikes(BoardLike boardLike){
        boardLikes.add(0,boardLike);
    }


    public void updateCategory(String category) {this.category = category;
    }
}
