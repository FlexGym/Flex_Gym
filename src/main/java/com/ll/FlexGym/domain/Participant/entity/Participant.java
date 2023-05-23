package com.ll.FlexGym.domain.Participant.entity;

import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
public class Participant extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Meeting meeting;

    private String Role;
}
