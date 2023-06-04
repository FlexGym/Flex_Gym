package com.ll.FlexGym.domain.ChatRoom.service;

import com.ll.FlexGym.domain.ChatMember.entity.ChatMember;
import com.ll.FlexGym.domain.ChatMember.repository.ChatMemberRepository;
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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMemberRepository chatMemberRepository;
    private final MemberService memberService;

    @Transactional
    public ChatRoom createAndSave(String name, Long ownerId) {

        Member owner = memberService.findByIdElseThrow(ownerId);

        ChatRoom chatRoom = ChatRoom.create(name, owner);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        log.info("savedChatRoom = {} ", savedChatRoom);

        log.info("Owner = {} ", owner);

        savedChatRoom.addChatUser(owner);

        return savedChatRoom;
    }

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow();
    }

    @Transactional
    public ChatRoomDto getByIdAndUserId(Long roomId, long memberId) {
        Member member = memberService.findByIdElseThrow(memberId);

        ChatRoom chatRoom = findById(roomId);

        addChatRoomMember(chatRoom, member, memberId);

        chatRoom.getChatMembers().stream()
                .filter(chatMember -> chatMember.getMember().getId().equals(memberId))
                .findFirst()
                .orElseThrow();

        return ChatRoomDto.fromChatRoom(chatRoom);
    }

    private void addChatRoomMember(ChatRoom chatRoom, Member member, Long memberId) {

        // 만약에 방에 해당 유저가 없다면 추가한다. 방법1
        Optional<ChatMember> existingMember = chatRoom.getChatMembers().stream()
                .filter(chatMember -> chatMember.getMember().getId().equals(memberId))
                .findFirst();

        log.info("memberId = {}", memberId);
        log.info("member.getId = {}", member.getId());

        if (existingMember.isEmpty()) {
            chatRoom.addChatUser(member);
        }
    }

    /**
     * 채팅방 삭제
     */
    @Transactional
    public void remove(Long roomId, Long OwnerId) {
        Member owner = memberService.findByIdElseThrow(OwnerId);
        log.info("roomId = {}", roomId);
        log.info("OnwerId = {}", owner.getId());

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));

        if(!chatRoom.getOwner().equals(owner)) {
            throw new IllegalArgumentException("방 삭제 권한이 없습니다.");
        }

        removeChatRoom(chatRoom);
    }

    public void removeChatRoom(ChatRoom chatRoom) {
        chatRoom.getChatMembers().clear();
        chatRoomRepository.delete(chatRoom);
    }


    /**
     * 유저가 방 나가기
     * 현재는 사용안하고 있음!
     */
    @Transactional
    public void exitChatRoom(Long roomId, Long memberId) {
        ChatRoom chatRoom = findById(roomId);
        log.info("memberId = {} ", memberId);

        // 해당 유저의 ChatMember를 제거합니다.
        ChatMember chatMember = findChatMemberByMemberId(chatRoom, memberId);
        log.info("chatMember = {} ", chatMember);
        if (chatMember != null) {
            chatRoom.removeChatMember(chatMember);
        }
    }

    private ChatMember findChatMemberByMemberId(ChatRoom chatRoom, Long memberId) {
        return chatRoom.getChatMembers().stream()
                .filter(chatMember -> chatMember.getMember().getId().equals(memberId))
                .findFirst()
                .orElse(null);
    }
}