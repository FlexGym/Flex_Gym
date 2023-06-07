package com.ll.FlexGym.domain.ChatRoom.controller;

import com.ll.FlexGym.domain.ChatRoom.dto.ChatRoomDto;
import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.ChatRoom.service.ChatRoomService;
import com.ll.FlexGym.global.security.SecurityMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/usr/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

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

        ChatRoomDto chatRoomDto = chatRoomService.getByIdAndUserId(roomId, member.getId());

        model.addAttribute("chatRoom", chatRoomDto);

        return "usr/chat/room";
    }

    /**
     * 방 생성
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/rooms/new")
    public String showNewRoom() {
        return "usr/chat/newRoom";
    }

    /**
     * 방 생성
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/rooms")
    public String newRoom(String roomName, @AuthenticationPrincipal SecurityMember member) {

        log.info("newRoom SecurityMember = {}", member);

        ChatRoom chatRoom = chatRoomService.createAndSave(roomName, member.getId());

        return "redirect:/usr/chat/rooms/" + chatRoom.getId();
    }

    /**
     * 채팅방 삭제(Owner만 가능)
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/rooms/{roomId}")
    public String removeRoom(@PathVariable Long roomId, @AuthenticationPrincipal SecurityMember member) {
        chatRoomService.remove(roomId, member.getId());
        return "redirect:/usr/chat/rooms";
    }

    /**
     * Member가 채팅방 나가기
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/rooms/{roomId}")
    public String exitChatRoom(@PathVariable Long roomId, @AuthenticationPrincipal SecurityMember member){
        chatRoomService.exitChatRoom(roomId, member.getId());

        return "redirect:/usr/chat/rooms";
    }

}
