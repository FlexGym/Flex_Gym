package com.ll.FlexGym.domain.Information.entity;

import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
public class Information extends BaseEntity {

    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    private Favorite favorite;
}
