package com.ll.FlexGym.domain.ChatMessage.entity;

import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
public class ChatMessage extends BaseEntity {

    private String sender;
    private String message;

    @ManyToOne
    private ChatRoom chatRoom; // 해당 채팅방 룸
}
