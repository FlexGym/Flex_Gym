package com.ll.FlexGym.domain.BoardLike.entity;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.User.entitiy.User;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
public class BoardLike extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

}
