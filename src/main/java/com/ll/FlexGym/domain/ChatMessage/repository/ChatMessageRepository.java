package com.ll.FlexGym.domain.ChatMessage.repository;

import com.ll.FlexGym.domain.ChatMessage.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    List<ChatMessage> findByChatRoomId(Long roomId);
}
