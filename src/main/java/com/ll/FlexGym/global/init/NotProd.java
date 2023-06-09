package com.ll.FlexGym.global.init;

import com.ll.FlexGym.domain.Board.service.BoardService;
import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.ChatRoom.service.ChatRoomService;
import com.ll.FlexGym.domain.Information.entity.InfoStatus;
import com.ll.FlexGym.domain.Information.entity.Information;
import com.ll.FlexGym.domain.Information.service.InformationService;
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
            MeetingService meetingService,
            InformationService informationService,
            BoardService boardService

    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Member memberUser1 = memberService.join("user1", "1234").getData();
                Member memberUser2 = memberService.join("user2", "1234").getData();
                Member memberUser3 = memberService.join("user3", "1234").getData();
                Member memberUser4 = memberService.join("user4", "1234").getData();
                Member memberUser5ByKakao = memberService.whenSocialLogin("KAKAO", "KAKAO__2844575793").getData();

                Meeting meeting1 = meetingService.create("오늘 한강에서 러닝하실 분 구합니다!!",
                        memberUser1, 8, "용산구", "2023-07-08", "14:00",
                        "한강에서 2시간 정도 같이 달리실 분 구합니다!");
                Meeting meeting2 = meetingService.create("이번주 토요일 바이크 타실 분 구합니다 :)",
                        memberUser2,3, "잠실", "2023-06-28", "10:00",
                        "오전에 같이 운동해요!");
                Meeting meeting3 = meetingService.create("등산모임 멤버 모집합니다!",
                        memberUser3,15, "종로구", "2023-05-28", "10:00",
                        "오전에 같이 운동해요!");
                Meeting meeting4 = meetingService.create("러닝크루 모집합니다 :)",
                        memberUser4,20, "여의도", "2023-06-28", "10:00",
                        "오전에 같이 운동해요!");
                Meeting meeting5 = meetingService.create("매주 토요일 조기 축구 모임",
                        memberUser4,5, "서초구", "2023-06-28", "10:00",
                        "오전에 같이 운동해요!");
                Meeting meeting6 = meetingService.create("마라톤 대회 같이 참여해요!!!",
                        memberUser3,3, "강남", "2023-06-28", "10:00",
                        "오전에 같이 운동해요!");
                Meeting meeting7 = meetingService.create("마포대교에서 같이 러닝하실 분",
                        memberUser2,8, "홍대", "2023-06-28", "10:00",
                        "오전에 같이 운동해요!");
                Meeting meeting8 = meetingService.create("풋살 모임 참여하실 분~~~",
                        memberUser1,10, "판교", "2023-06-28", "10:00",
                        "오전에 같이 운동해요!");
                Meeting meeting9 = meetingService.create("잠실대교에서 같이 러닝해요",
                        memberUser4,9, "잠실","2023-06-28", "10:00",
                        "오전에 같이 운동해요!");
                Meeting meeting10 = meetingService.create("매주 등산하는 프로등산러 모임",
                        memberUser3,3, "고양", "2023-06-28", "10:00",
                        "오전에 같이 운동해요!");
                Meeting meeting11 = meetingService.create("이번주 토요일 바이크 타실 분!!",
                        memberUser2,5, "광교", "2023-06-28", "10:00",
                        "오전에 같이 운동해요!");
                Meeting meeting12 = meetingService.create("RUNNING CREW 모집 :)",
                        memberUser1,3, "영등포", "2023-06-28", "10:00",
                        "오전에 같이 운동해요!");

                ChatRoom chatRoom1 = chatRoomService.createAndConnect(meeting1.getSubject(), meeting1, memberUser1.getId());
                ChatRoom chatRoom2 = chatRoomService.createAndConnect(meeting2.getSubject(), meeting2, memberUser2.getId());
                ChatRoom chatRoom3 = chatRoomService.createAndConnect(meeting3.getSubject(), meeting3, memberUser3.getId());
                ChatRoom chatRoom4 = chatRoomService.createAndConnect(meeting4.getSubject(), meeting4, memberUser4.getId());
                ChatRoom chatRoom5 = chatRoomService.createAndConnect(meeting5.getSubject(), meeting5, memberUser4.getId());
                ChatRoom chatRoom6 = chatRoomService.createAndConnect(meeting6.getSubject(), meeting6, memberUser3.getId());
                ChatRoom chatRoom7 = chatRoomService.createAndConnect(meeting7.getSubject(), meeting7, memberUser2.getId());
                ChatRoom chatRoom8 = chatRoomService.createAndConnect(meeting8.getSubject(), meeting8, memberUser1.getId());
                ChatRoom chatRoom9 = chatRoomService.createAndConnect(meeting9.getSubject(), meeting9, memberUser4.getId());
                ChatRoom chatRoom10 = chatRoomService.createAndConnect(meeting10.getSubject(), meeting10, memberUser3.getId());
                ChatRoom chatRoom11 = chatRoomService.createAndConnect(meeting11.getSubject(), meeting11, memberUser2.getId());
                ChatRoom chatRoom12 = chatRoomService.createAndConnect(meeting12.getSubject(), meeting12, memberUser1.getId());

                Information information1 = informationService.create("H-AcDBLqxi4","[운동자극]피곤하고 귀찮을때 보는 동기부여영상","https://i.ytimg.com/vi/H-AcDBLqxi4/0.jpg");
                Information information2 = informationService.create("7A08jgKKRvk","[운동자극]피곤하고 귀찮을때 보는 동기부여영상 | 하드 공격적 운동 힙합 음악 체육관 훈련 동기 부여 ( 운동 음악 )","https://i.ytimg.com/vi/7A08jgKKRvk/0.jpg");
                Information information3 = informationService.create("gMaB-fG4u4g","전신 다이어트 최고의 운동 [칼소폭 찐 핵핵매운맛]","https://i.ytimg.com/vi/gMaB-fG4u4g/0.jpg");
                Information information4 = informationService.create("0uixp1vmKKY","최초공개 흑자헬스 운동영상 - 스쾃","https://i.ytimg.com/vi/0uixp1vmKKY/0.jpg");
                Information information5 = informationService.create("Hx8Lc_0hUaI","운동할 시간이 없다는 사람에게 보여주세요... 제발","https://i.ytimg.com/vi/Hx8Lc_0hUaI/0.jpg");
                Information information6 = informationService.createProd("Hx8Lc_0hUaI","6번째 데이터입니다","https://i.ytimg.com/vi/Hx8Lc_0hUaI/0.jpg", InfoStatus.ON);
                Information information7 = informationService.createProd("Hx8Lc_0hUaI","7번째 데이터입니다","https://i.ytimg.com/vi/Hx8Lc_0hUaI/0.jpg", InfoStatus.ON);

                boardService.create("오늘도 운동 열심히!","1","꾸준히 하는 것이 중요합니다~", memberUser1);
                boardService.create("상체상체상체","2","상체운동최고", memberUser2);
                boardService.create("하체하체하체","3","하체운동최고", memberUser1);
                boardService.create("바디프로필","4","바디프로필이 목표입니다",memberUser3);
                boardService.create("오늘의 식단~","5","닭가슴살과 당근쥬스",memberUser4);
                boardService.create("오늘도 운동 열심히!!!!","1","꾸준히 하는 것이 중요합니다~", memberUser1);
                boardService.create("상체상체상체~~~~~","2","상체운동최고", memberUser2);
                boardService.create("하체하체하체!!!!!!","3","하체운동최고", memberUser1);
                boardService.create("바디프로필!!!","4","바디프로필이 목표입니다",memberUser3);
                boardService.create("오늘의 식단~~~~~","5","닭가슴살과 당근쥬스",memberUser4);
                boardService.create("오늘도 운동 열심히!~!~!~","1","꾸준히 하는 것이 중요합니다~", memberUser1);
                boardService.create("상체상체상체!!!!!!1","2","상체운동최고", memberUser2);
                boardService.create("하체하체하체~~","3","하체운동최고", memberUser1);
                boardService.create("바디프로필!~!","4","바디프로필이 목표입니다",memberUser3);
                boardService.create("오늘의 식단~~~~","5","닭가슴살과 당근쥬스",memberUser4);


            }
        };
    }
}