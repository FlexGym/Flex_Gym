package com.ll.FlexGym.domain.ChatMessage.controller;

import com.ll.FlexGym.domain.ChatMessage.dto.ChatMessageDto;
import com.ll.FlexGym.domain.ChatMessage.dto.request.ChatMessageRequest;
import com.ll.FlexGym.domain.ChatMessage.dto.response.SignalResponse;
import com.ll.FlexGym.domain.ChatMessage.service.ChatMessageService;
import com.ll.FlexGym.global.security.SecurityMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.ll.FlexGym.domain.ChatMessage.dto.response.SignalType.NEW_MESSAGE;
import static com.ll.FlexGym.domain.ChatMessage.entity.ChatMessageType.MESSAGE;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chats/{roomId}/sendMessage") // app/chats/{roomId}/sendMessage
    @SendTo("/topic/chats/{roomId}") // 다시보내는 경로? enableSimpleBroker
    public SignalResponse sendChatMessage(@DestinationVariable Long roomId, ChatMessageRequest request,
                                          @AuthenticationPrincipal SecurityMember member)  {

        log.info("content : {}", request.getContent());
        log.info("sendChatMessage SecurityMember = {}", member);

        chatMessageService.createAndSave(request.getContent(), member.getId(), roomId, MESSAGE);

        return SignalResponse.builder()
                .type(NEW_MESSAGE)
                .build();
    }

    @MessageExceptionHandler
    public void handleException(Exception ex) {
        System.out.println("예외 발생!!");
    }

    @GetMapping("/usr/chat/rooms/{roomId}/messages")
    @ResponseBody
    public List<ChatMessageDto> findAll(
            @PathVariable Long roomId, @AuthenticationPrincipal SecurityMember member,
            @RequestParam(defaultValue = "0") Long fromId) {

        log.info("findAll SecurityMember = {}", member);

        List<ChatMessageDto> chatMessageDtos =
                chatMessageService.getByChatRoomIdAndUserIdAndFromId(roomId, member.getId(), fromId);

        return chatMessageDtos;
    }
}
