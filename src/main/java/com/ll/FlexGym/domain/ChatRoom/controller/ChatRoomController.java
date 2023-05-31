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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/usr/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/rooms")
    public String showRooms(Model model) {

        List<ChatRoom> chatRooms = chatRoomService.findAll();

        model.addAttribute("chatRooms", chatRooms);
        return "usr/chat/rooms";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/rooms/{roomId}")
    public String showRoom(@PathVariable Long roomId, Model model, @AuthenticationPrincipal SecurityMember member) {

        log.info("showRoom SecurityMember = {}", member);

        ChatRoomDto chatRoomDto = chatRoomService.getByIdAndUserId(roomId, member.getId());

        model.addAttribute("chatRoom", chatRoomDto);

        return "usr/chat/room";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/rooms/new")
    public String showNewRoom() {
        return "usr/chat/newRoom";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/rooms")
    public String newRoom(String roomName, @AuthenticationPrincipal SecurityMember member) {

        log.info("newRoom SecurityMember = {}", member);

        ChatRoom chatRoom = chatRoomService.createAndSave(roomName, member.getId());

        return "redirect:/usr/chat/rooms/" + chatRoom.getId();
    }
}
