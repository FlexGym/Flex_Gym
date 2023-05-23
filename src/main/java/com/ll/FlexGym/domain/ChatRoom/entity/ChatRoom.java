package com.ll.FlexGym.domain.ChatRoom.entity;

import com.ll.FlexGym.domain.ChatMessage.entity.ChatMessage;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class ChatRoom extends BaseEntity {

    @OneToOne(fetch = LAZY)
    private Meeting meeting;

    @OneToMany(mappedBy = "chatRoom", cascade = {CascadeType.ALL})
    private List<ChatMessage> chatMessage = new ArrayList<>();

}
