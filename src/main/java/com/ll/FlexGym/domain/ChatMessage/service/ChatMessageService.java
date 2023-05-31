package com.ll.FlexGym.domain.ChatMessage.service;

import com.ll.FlexGym.domain.ChatMessage.dto.ChatMessageDto;
import com.ll.FlexGym.domain.ChatMessage.entity.ChatMessage;
import com.ll.FlexGym.domain.ChatMessage.entity.ChatMessageType;
import com.ll.FlexGym.domain.ChatMessage.repository.ChatMessageRepository;
import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.ChatRoom.service.ChatRoomService;
import com.ll.FlexGym.domain.chatUser.entity.ChatMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessage createAndSave(String content, Long senderId, Long chatRoomId, ChatMessageType type) {

        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);

        ChatMember sender = chatRoom.getChatMembers().stream()
                .filter(chatMember -> chatMember.getMember().getId().equals(senderId))
                .findFirst()
                .orElseThrow();

        ChatMessage chatMessage = ChatMessage.builder()
                .content(content)
                .sender(sender)
                .type(type)
                .chatRoom(chatRoom)
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageDto> getByChatRoomIdAndUserIdAndFromId(Long roomId, Long userId, Long fromId) {

        ChatRoom chatRoom = chatRoomService.findById(roomId);

        chatRoom.getChatMembers().stream()
                .filter(chatMember -> chatMember.getMember().getId().equals(userId))
                .findFirst()
                .orElseThrow();

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(roomId);

        List<ChatMessage> list = chatMessages.stream()
                .filter(chatMessage -> chatMessage.getId() > fromId)
                .sorted(Comparator.comparing(ChatMessage::getId))
                .toList();

        return ChatMessageDto.fromChatMessages(list);
    }
}
