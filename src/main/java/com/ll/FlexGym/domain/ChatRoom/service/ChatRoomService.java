package com.ll.FlexGym.domain.ChatRoom.service;

import com.ll.FlexGym.domain.ChatMember.entity.ChatMember;
import com.ll.FlexGym.domain.ChatMember.service.ChatMemberService;
import com.ll.FlexGym.domain.ChatMessage.entity.ChatMessage;
import com.ll.FlexGym.domain.ChatRoom.dto.ChatRoomDto;
import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.ChatRoom.repository.ChatRoomRepository;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.service.MemberService;
import com.ll.FlexGym.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ll.FlexGym.domain.ChatMember.entity.ChatMemberType.KICKED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;
    private final ChatMemberService chatMemberService;

    @Transactional
    public ChatRoom createAndConnect(String subject, Meeting meeting, Long ownerId) {
        Member owner = memberService.findByIdElseThrow(ownerId);
        ChatRoom chatRoom = ChatRoom.create(subject, meeting, owner);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        log.info("savedChatRoom = {} ", savedChatRoom);
        log.info("Owner = {} ", owner);
        log.info("meeting = {}", meeting);

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

        boolean isNew = addChatRoomMember(chatRoom, member, memberId);

        chatRoom.getChatMembers().stream()
                .filter(chatMember -> chatMember.getMember().getId().equals(memberId))
                .findFirst()
                .orElseThrow();

        return ChatRoomDto.fromChatRoom(chatRoom, isNew);
    }

    private Optional<ChatMember> getChatUser(ChatRoom chatRoom, Member member, Long memberId) {
        // 방에 해당 유저가 있으면 가져오기
        Optional<ChatMember> existingMember = chatRoom.getChatMembers().stream()
                .filter(chatMember -> chatMember.getMember().getId().equals(memberId))
                .findFirst();

        log.info("memberId = {}", memberId);
        log.info("member.getId = {}", member.getId());

        return existingMember;
    }

    private boolean addChatRoomMember(ChatRoom chatRoom, Member member, Long memberId) {

        if (getChatUser(chatRoom, member, memberId).isEmpty()) {
            chatRoom.addChatUser(member);
            chatRoom.getMeeting().increaseParticipantsCount(); // 유저가 참여하면 '현재 참여자 수' 1 증가
            return true;
        }

        return false;
    }

    // 참여자 추가 가능한지 확인하는 메서드
    public RsData canAddChatRoomMember(ChatRoom chatRoom, Long memberId, Meeting meeting) {

        Member member = memberService.findByIdElseThrow(memberId);

        // 이미 채팅방에 동일 유저가 존재하는 경우
        if (!getChatUser(chatRoom, member, memberId).isEmpty()) {
            ChatMember chatMember = getChatUser(chatRoom, member, memberId).get();
            return checkChatMemberType(chatMember);
        }

        if (!meeting.canAddParticipant()) return RsData.of("F-2", "모임 정원 초과!");

        return RsData.of("S-1", "새로운 모임 채팅방에 참여합니다.");
    }

    public RsData checkChatMemberType(ChatMember chatMember) {
        if (chatMember.getType().equals(KICKED)) return RsData.of("F-1", "강퇴당한 모임입니다!");

        return RsData.of("S-1", "기존 모임 채팅방에 참여합니다.");
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
        chatMember.getChatMessages().clear();

        if (chatMember != null) {
            chatRoom.removeChatMember(chatMember);
        }

        chatRoom.getMeeting().decreaseParticipantsCount(); // 유저가 나가면 '현재 참여자 수' 1 감소
    }

    private ChatMember findChatMemberByMemberId(ChatRoom chatRoom, Long memberId) {
        return chatRoom.getChatMembers().stream()
                .filter(chatMember -> chatMember.getMember().getId().equals(memberId))
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public void updateChatRoomName(ChatRoom chatRoom, String subject) {
        log.info("update subject = {}", subject);
        chatRoom.updateName(subject);
        chatRoomRepository.save(chatRoom);
    }

    // 유저 강퇴하기
    @Transactional
    public void kickChatMember(Long chatMemberId) {
        ChatMember chatMember = chatMemberService.findById(chatMemberId);
        ChatRoom chatRoom = chatMember.getChatRoom();

        chatMember.changeType(); // 강퇴된 유저의 type 을 "KICKED"로 변경

        List<ChatMessage> chatMessages = chatRoom.getChatMessages();

        chatMessages.stream()
                .filter(chatMessage -> chatMessage.getSender().getId().equals(chatMemberId))
                .forEach(chatMessage -> chatMessage.removeChatMessages("강퇴된 사용자의 메시지입니다."));

        chatRoom.getMeeting().decreaseParticipantsCount(); // 유저가 강퇴되면 '현재 참여자 수' 1 감소
    }
}