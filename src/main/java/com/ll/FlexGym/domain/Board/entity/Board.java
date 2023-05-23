package com.ll.FlexGym.domain.Board.entity;

import com.ll.FlexGym.domain.BoardLike.entity.BoardLike;
import com.ll.FlexGym.domain.Comment.entity.Comment;
import com.ll.FlexGym.domain.User.entitiy.User;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
public class Board extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL})
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL})
    private List<BoardLike> boardLikes = new ArrayList<>();

    private String title;
    private String content;
    private Long views;
    private Category Category;
}
