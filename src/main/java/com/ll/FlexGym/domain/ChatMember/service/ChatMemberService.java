package com.ll.FlexGym.domain.ChatMember.service;

import com.ll.FlexGym.domain.ChatMember.entity.ChatMember;
import com.ll.FlexGym.domain.ChatMember.repository.ChatMemberRepository;
import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.ChatRoom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMember findById(Long chatRoomUserId) {
        return chatMemberRepository.findById(chatRoomUserId).orElseThrow();
    }

    public List<ChatMember> findByChatRoomId(Long chatRoomId) {
        return chatMemberRepository.findByChatRoomId(chatRoomId);
    }

    public List<ChatMember> findByChatRoomIdAndChatMember(Long roomId, Long memberId) {
        ChatRoom chatRoom = findByRoomId(roomId);
        ChatMember chatMember = findChatMemberByMemberId(chatRoom, memberId);

        if (chatMember == null) {
            return null;
        }

        return chatMemberRepository.findByChatRoomId(roomId);
    }

    private ChatMember findChatMemberByMemberId(ChatRoom chatRoom, Long memberId) {
        return chatRoom.getChatMembers().stream()
                .filter(chatMember -> chatMember.getMember().getId().equals(memberId))
                .findFirst()
                .orElse(null);
    }

    public ChatRoom findByRoomId(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow();
    }
}
