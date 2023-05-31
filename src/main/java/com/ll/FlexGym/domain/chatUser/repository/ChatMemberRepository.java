package com.ll.FlexGym.domain.chatUser.repository;

import com.ll.FlexGym.domain.chatUser.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

}
