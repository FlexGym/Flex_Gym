package com.ll.FlexGym.domain.ChatMessage.entity;

import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.ChatMember.entity.ChatMember;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.Assert;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@SuperBuilder
public class ChatMessage extends BaseEntity {

    private String content; // 내용

    @ManyToOne(fetch = LAZY)
    private ChatMember sender; // 작성자

    @ManyToOne(fetch = LAZY)
    private ChatRoom chatRoom; // 해당 채팅방 룸

    @Enumerated(STRING)
    private ChatMessageType type;

    @Builder
    public ChatMessage(String content, ChatMember sender, ChatRoom chatRoom, ChatMessageType type) {

        Assert.notNull(content, "content는 널일 수 없습니다.");
        Assert.notNull(sender, "sender는 널일 수 없습니다.");
        Assert.notNull(chatRoom, "chatRoom는 널일 수 없습니다.");

        this.content = content;
        this.sender = sender;
        this.chatRoom = chatRoom;
    }

    public static ChatMessage create(String content, ChatMember chatMember, ChatMessageType chatMessageType, ChatRoom chatRoom) {

        ChatMessage chatMessage = ChatMessage.builder()
                .content(content)
                .sender(chatMember)
                .type(chatMessageType)
                .chatRoom(chatRoom)
                .build();

        return chatMessage;
    }

    public void removeChatMessages(String content){
        this.content = content;
    }
}
