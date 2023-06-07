package com.ll.FlexGym.domain.Meeting.entity;

import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.Participant.entity.Participant;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import com.ll.FlexGym.global.rsData.RsData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
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

    private String subject;
    private Integer capacity; // 참여 가능 인원
    private Integer nowParticipantsNum; // 현재 참여자 수
    private String location;
    private String dateTime;
    private String content;
    private LocalDateTime modifyDate;

    public RsData update(String subject, Integer capacity, String location, String dateTime, String content) {

        this.subject = subject;
        this.capacity = capacity;
        this.location = location;
        this.dateTime = dateTime;
        this.content = content;

        return RsData.of("S-1", "성공");
    }
}
