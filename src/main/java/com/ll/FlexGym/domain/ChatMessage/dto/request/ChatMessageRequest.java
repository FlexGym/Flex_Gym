package com.ll.FlexGym.domain.ChatMessage.dto.request;

import com.ll.FlexGym.domain.ChatMessage.entity.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessageRequest {
    private String content;
    private ChatMessageType type;
}
