package com.ll.FlexGym.domain.Favorite.entity;

import com.ll.FlexGym.domain.Information.entity.Information;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.*;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
public class Favorite extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Member member;


    @OneToOne(mappedBy ="favorite", fetch = LAZY)
    private Information information;
}