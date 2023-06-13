package com.ll.FlexGym.domain.Information.entity;

import com.ll.FlexGym.domain.BoardLike.entity.BoardLike;
import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.*;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
public class Information extends BaseEntity {

    private String content;

    private String videoId;
    private String title;
    private String videoThumnailUrl;

    @Enumerated(STRING)
    private InfoStatus status;

    @OneToOne(fetch = LAZY)
    private Favorite favorite;

}
