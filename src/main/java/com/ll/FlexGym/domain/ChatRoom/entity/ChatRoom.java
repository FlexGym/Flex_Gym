package com.ll.FlexGym.domain.ChatRoom.entity;

import com.ll.FlexGym.domain.ChatMember.entity.ChatMember;
import com.ll.FlexGym.domain.ChatMessage.entity.ChatMessage;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
@EqualsAndHashCode(of = {"id"}, callSuper = true)
public class ChatRoom extends BaseEntity {

    private String name;

    @OneToOne(fetch = LAZY)
    private Meeting meeting;

    @ManyToOne(fetch = LAZY)
    private Member owner;

    @OneToMany(mappedBy = "chatRoom", cascade = PERSIST, orphanRemoval = true)
    @Builder.Default
    private Set<ChatMember> chatMembers = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom", cascade = PERSIST, orphanRemoval = true )
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public static ChatRoom create(String name, Member owner) {

        Assert.notNull(name, "name는 널일 수 없습니다.");
        Assert.notNull(owner, "owner는 널일 수 없습니다.");

        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .owner(owner)
                .build();

        return chatRoom;
    }

    public void addChatUser(Member owner) {
        ChatMember chatMember = ChatMember.builder()
                .member(owner)
                .chatRoom(this)
                .build();

        chatMembers.add(chatMember);
    }

    public void removeChatMember(ChatMember chatMember) {
        chatMembers.remove(chatMember);
    }
}
