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

                Member memberUser1 = memberService.join("user1", "1234").getData();
                Member memberUser2 = memberService.join("user2", "1234").getData();
                Member memberUser3 = memberService.join("user3", "1234").getData();
                Member memberUser4 = memberService.join("user4", "1234").getData();




                Meeting meeting2 = meetingService.create("이번주 토요일 바이크 타실 분 구합니다.",
                        memberUser2,3, "마포구", "2023-06-28 10:00",
                        "오전에 같이 운동해요!");


            }
        };
    }
}