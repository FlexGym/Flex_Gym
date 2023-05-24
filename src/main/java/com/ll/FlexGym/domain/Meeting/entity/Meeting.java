package com.ll.FlexGym.domain.Meeting.entity;

import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.Participant.entity.Participant;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
public class Meeting extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @OneToOne(mappedBy = "meeting", fetch = LAZY)
    private ChatRoom chatRoom;

    //Participant
    @OneToMany(mappedBy = "meeting")
    private List<Participant> participants;

    //ChatRoom 미반영

    private String meetingName;
    private Integer capacity;
    private String location;
    private String dateTime;
    private String content;

}
