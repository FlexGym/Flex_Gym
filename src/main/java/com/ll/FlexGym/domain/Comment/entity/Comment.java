package com.ll.FlexGym.domain.Comment.entity;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.CommentLIke.entity.CommentLike;
import com.ll.FlexGym.domain.Member.entitiy.Member;
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
    private Member member;

    @ManyToOne(fetch = LAZY)
    private Board board;

    @OneToMany(mappedBy = "comment", cascade = {CascadeType.ALL})
    private List<CommentLike> commentLikes = new ArrayList<>();

    /**
     * 부모 댓글과 자식 추가
     * 셀프 조인
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> childComments = new ArrayList<>();


    public void updateContent(String content){this.content = content;}

    public void addToCommentLikes(CommentLike commentLike){
        commentLikes.add(0,commentLike);
    }


    public void addChildComment(Comment comment) {
        childComments.add(comment);
    }
}
