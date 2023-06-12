package com.ll.FlexGym.domain.ChatMember.service;

import com.ll.FlexGym.domain.ChatMember.entity.ChatMember;
import com.ll.FlexGym.domain.ChatMember.repository.ChatMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;

    public ChatMember findById(Long chatRoomUserId) {
        return chatMemberRepository.findById(chatRoomUserId).orElseThrow();
    }

    public List<ChatMember> findByChatRoomId(Long chatRoomId) {
        return chatMemberRepository.findByChatRoomId(chatRoomId);
    }
}
