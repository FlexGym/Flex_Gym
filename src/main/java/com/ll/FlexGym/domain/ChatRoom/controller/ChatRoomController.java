package com.ll.FlexGym.domain.ChatRoom.controller;

import com.ll.FlexGym.domain.ChatMember.entity.ChatMember;
import com.ll.FlexGym.domain.ChatMember.service.ChatMemberService;
import com.ll.FlexGym.domain.ChatMessage.dto.response.SignalResponse;
import com.ll.FlexGym.domain.ChatMessage.service.ChatMessageService;
import com.ll.FlexGym.domain.ChatRoom.dto.ChatRoomDto;
import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.ChatRoom.service.ChatRoomService;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.global.rq.Rq;
import com.ll.FlexGym.global.rsData.RsData;
import com.ll.FlexGym.global.security.SecurityMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ll.FlexGym.domain.ChatMember.entity.ChatMemberType.KICKED;
import static com.ll.FlexGym.domain.ChatMessage.dto.response.SignalType.*;
import static com.ll.FlexGym.domain.ChatMessage.entity.ChatMessageType.ENTER;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/usr/chat")
public class ChatRoomController {

    private final Rq rq;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations template;
    private final ChatMemberService chatMemberService;

    /**
     * 방 조회
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/rooms")
    public String showRooms(Model model) {

        List<ChatRoom> chatRooms = chatRoomService.findAll();

        model.addAttribute("chatRooms", chatRooms);
        return "usr/chat/rooms";
    }

    /**
     * 방 입장
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/rooms/{roomId}")
    public String showRoom(@PathVariable Long roomId, Model model, @AuthenticationPrincipal SecurityMember member) {

        log.info("showRoom SecurityMember = {}", member);
        log.info("member.getUsername = {}", member.getUsername());
        log.info("member.getAuthorities = {}", member.getAuthorities());
        log.info("member.getId = {}", member.getId());

        ChatRoom chatRoom = chatRoomService.findById(roomId);
        Meeting meeting = chatRoom.getMeeting();

        RsData rsData = chatRoomService.canAddChatRoomMember(chatRoom, member.getId(), meeting);

        if (rsData.isFail()) return rq.historyBack(rsData);

        ChatRoomDto chatRoomDto = chatRoomService.getByIdAndUserId(roomId, member.getId());

        if (chatRoomDto.isNew()) {
            chatMessageService.createAndSave(" < " + member.getUsername() + "님이 입장하셨습니다. >", member.getId(), roomId, ENTER);
            // Enter 로 들어올 경우!
            SignalResponse signalResponse = SignalResponse.builder()
                    .type(NEW_MESSAGE)
                    .build();
            template.convertAndSend("/topic/chats/room/" + chatRoom.getId(), signalResponse);
        }

        model.addAttribute("chatRoom", chatRoomDto);
        model.addAttribute("member", member);

        return "usr/chat/room";
    }

    /**
     * 채팅방 삭제(Owner만 가능)
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/rooms/{roomId}")
    public String removeRoom(@PathVariable Long roomId, @AuthenticationPrincipal SecurityMember member) {
        chatRoomService.remove(roomId, member.getId());
        return "redirect:/usr/meeting/list";
    }

    /**
     * Member가 채팅방 나가기
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/rooms/{roomId}")
    public String exitChatRoom(@PathVariable Long roomId, @AuthenticationPrincipal SecurityMember member){
        chatRoomService.exitChatRoom(roomId, member.getId());

        return "redirect:/usr/meeting/list";
    }

    // 방장이 유저 강퇴시키기
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/rooms/kick/{id}")
    public String kickChatMember(@PathVariable Long id){
        chatRoomService.kickChatMember(id);

        return "redirect:/usr/meeting/list";
    }

    // 유저 정보 가져오기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{roomId}/memberList")
    public String memberList(Model model, @PathVariable Long roomId,
                             @AuthenticationPrincipal SecurityMember member) {
        List<ChatMember> chatMemberList = chatMemberService.findByChatRoomIdAndChatMember(roomId, member.getId());
        ChatRoom chatRoom = chatRoomService.findById(roomId);

        if (chatMemberList == null) {
            return rq.historyBack("해당 방에 참가하지 않았습니다.");
        }

        model.addAttribute("chatMemberList", chatMemberList);
        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("KICKED", KICKED);
        return "usr/chat/memberList";
    }
}
