package com.ll.FlexGym.domain.Comment.entity;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.BoardLike.entity.BoardLike;
import com.ll.FlexGym.domain.CommentLIke.entity.CommentLike;
import com.ll.FlexGym.domain.User.entitiy.User;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

    @Getter
    @RequiredArgsConstructor
    @Entity
    @SuperBuilder
    public class Comment extends BaseEntity {

        private String content;

        @ManyToOne(fetch = LAZY)
        private User user;

        @ManyToOne(fetch = LAZY)
        private Board board;

        @OneToMany(mappedBy = "comment", cascade = {CascadeType.ALL})
        private List<CommentLike> commentLikes = new ArrayList<>();

        /**
         * 부모 댓글과 자식 추가
         * 셀프 조인
         */
        @ManyToOne(fetch = LAZY)
        @JoinColumn(name = "parent_id")
        private Comment parent;

        @Builder.Default
        @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
        private List<Comment> children = new ArrayList<>();

    }
