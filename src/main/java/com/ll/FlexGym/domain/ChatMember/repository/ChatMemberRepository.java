package com.ll.FlexGym.domain.ChatMember.repository;

import com.ll.FlexGym.domain.ChatMember.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

}
