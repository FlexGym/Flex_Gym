package com.ll.FlexGym.domain.ChatMessage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.FlexGym.domain.ChatMessage.entity.ChatMessage;
import com.ll.FlexGym.domain.ChatMessage.entity.ChatMessageType;
import com.ll.FlexGym.domain.Member.dto.MemberDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {

    @JsonProperty("message_id")
    private Long id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("sender")
    private MemberDto sender;

    @JsonProperty("type")
    private ChatMessageType type;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static ChatMessageDto fromChatMessage(ChatMessage chatMessage) {

        MemberDto userDto = MemberDto.fromUser(chatMessage.getSender().getMember());

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .id(chatMessage.getId())
                .type(chatMessage.getType())
                .sender(userDto)
                .content(chatMessage.getContent())
                .type(chatMessage.getType())
                .createdAt(chatMessage.getCreateDate())
                .updatedAt(chatMessage.getModifyDate())
                .build();

        return chatMessageDto;
    }

    public static List<ChatMessageDto> fromChatMessages(List<ChatMessage> messages) {
        return messages.stream()
                .map(ChatMessageDto::fromChatMessage)
                .toList();
    }

}
