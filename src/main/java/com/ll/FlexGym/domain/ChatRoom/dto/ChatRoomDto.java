package com.ll.FlexGym.domain.ChatRoom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.Member.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {

    @JsonProperty("chat_room_id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("owner")
    private MemberDto owner;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    private boolean isNew;

    public static ChatRoomDto fromChatRoom(ChatRoom chatRoom, boolean isNew) {
        MemberDto userDto = MemberDto.fromUser(chatRoom.getOwner());

        ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .owner(userDto)
                .createdAt(chatRoom.getCreateDate())
                .updatedAt(chatRoom.getModifyDate())
                .isNew(isNew)
                .build();

        return chatRoomDto;
    }
}
