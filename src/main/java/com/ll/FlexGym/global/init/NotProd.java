package com.ll.FlexGym.global.init;

import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.ChatRoom.service.ChatRoomService;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Meeting.service.MeetingService;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@Profile({"local", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ChatRoomService chatRoomService,
            MeetingService meetingService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
//                Member memberUser1 = memberService.join("user1", "1234").getData();
//                Member memberUser2 = memberService.join("user2", "1234").getData();
//                Member memberUser3 = memberService.join("user3", "1234").getData();

//                ChatRoom chatRoom1 = chatRoomService.createAndSave("환영합니다.1", memberUser1.getId());
//                ChatRoom chatRoom2 = chatRoomService.createAndSave("환영합니다.2", memberUser2.getId());
//
//                Meeting meeting1 = meetingService.create("오늘 한강에서 러닝하실 분 구합니다.",
//                        memberUser1, 8, "여의도", "2023-07-08 14:00",
//                        "한강에서 2시간 정도 같이 달리실 분 구합니다!");
//
//                Meeting meeting2 = meetingService.create("이번주 토요일 바이크 타실 분 구합니다.",
//                        memberUser2,4, "마포구", "2023-06-28 10:00",
//                        "오전에 같이 운동해요!");
            }
        };
    }
}