package com.ll.FlexGym.domain.ChatMember.repository;

import com.ll.FlexGym.domain.ChatMember.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    List<ChatMember> findByChatRoomId(Long chatRoomId);
}
