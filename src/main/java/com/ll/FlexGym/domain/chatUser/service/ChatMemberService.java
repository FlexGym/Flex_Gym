package com.ll.FlexGym.domain.chatUser.service;

import com.ll.FlexGym.domain.chatUser.entity.ChatMember;
import com.ll.FlexGym.domain.chatUser.repository.ChatMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;


    public ChatMember findById(Long chatRoomUserId) {
        return chatMemberRepository.findById(chatRoomUserId).orElseThrow();
    }
}
