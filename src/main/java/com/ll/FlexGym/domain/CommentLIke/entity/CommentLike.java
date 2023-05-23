package com.ll.FlexGym.domain.CommentLIke.entity;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Comment.entity.Comment;
import com.ll.FlexGym.domain.User.entitiy.User;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.*;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
public class CommentLike extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Comment comment;

    @ManyToOne(fetch = LAZY)
    private User user;

}
