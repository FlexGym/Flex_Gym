package com.ll.FlexGym.domain.Meeting.service;

import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Meeting.repository.MeetingRepository;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MeetingService {
    private final MeetingRepository meetingRepository;

    public List<Meeting> getList() {
        return this.meetingRepository.findAll();
    }

    public Optional<Meeting> getMeeting(Integer id) {
        return meetingRepository.findById(id);
    }

    @Transactional
    public Meeting create(String subject, Member member, Integer capacity, String location, String dateTime, String content) {
        Meeting meeting = Meeting
                .builder()
                .subject(subject)
                .member(member)
                .capacity(capacity)
                .nowParticipantsNum(0)
                .location(location)
                .dateTime(dateTime)
                .content(content)
                .build();

        meetingRepository.save(meeting);
        return meeting;
    }

    @Transactional
    public RsData delete(Meeting meeting) {
        meetingRepository.delete(meeting);

        return RsData.of("S-1", "모임을 삭제하였습니다.");
    }

    public RsData canDelete(Member actor, Meeting meeting) {
        if (meeting == null) return RsData.of("F-1", "이미 삭제되었습니다.");

        // 로그인한 멤버의 id
        long actorMemberId = actor.getId();

        // 삭제하려는 모임 작성자의 멤버 id
        long meetingMemberId = meeting.getMember().getId();

        if (actorMemberId != meetingMemberId)
            return RsData.of("F-2", "취소할 권한이 없습니다.");

        return RsData.of("S-1", "취소가 가능합니다.");
    }
}
