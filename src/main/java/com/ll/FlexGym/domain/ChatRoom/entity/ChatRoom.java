package com.ll.FlexGym.domain.ChatRoom.entity;

import com.ll.FlexGym.domain.ChatMessage.entity.ChatMessage;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
public class ChatRoom extends BaseEntity {
    private LocalDateTime createdDate;
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "chatRoom", cascade = {CascadeType.ALL})
    private List<ChatMessage> chatMessage = new ArrayList<>();



}
