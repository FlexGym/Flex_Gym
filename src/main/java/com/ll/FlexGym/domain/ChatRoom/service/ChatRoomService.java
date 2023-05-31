package com.ll.FlexGym.domain.ChatRoom.service;

import com.ll.FlexGym.domain.ChatRoom.dto.ChatRoomDto;
import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.ChatRoom.repository.ChatRoomRepository;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;

    public ChatRoom createAndSave(String name, Long ownerId) {

        Member owner = memberService.findByIdElseThrow(ownerId);

        ChatRoom chatRoom = ChatRoom.create(name, owner);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        chatRoom.addChatUser(owner);

        return savedChatRoom;
    }

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow();
    }

    public ChatRoomDto getByIdAndUserId(Long roomId, long memberId) {
        ChatRoom chatRoom = findById(roomId);

        chatRoom.getChatMembers().stream()
                .filter(chatMember -> chatMember.getMember().getId().equals(memberId))
                .findFirst()
                .orElseThrow();

        return ChatRoomDto.fromChatRoom(chatRoom);
    }
}