package com.ll.FlexGym.domain.youtube.entity;

import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
public class YoutubeEntity extends BaseEntity {

    private String videoId;
    private String title;
    private String videoUrl;
//    @OneToOne(fetch = LAZY)
//    private Favorite favorite;

}
