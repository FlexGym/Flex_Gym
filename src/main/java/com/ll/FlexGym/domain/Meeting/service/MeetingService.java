package com.ll.FlexGym.domain.Meeting.service;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Meeting.repository.MeetingRepository;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.repository.MemberRepository;
import com.ll.FlexGym.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;

    public Page<Meeting> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 9, Sort.by(sorts));
        return meetingRepository.findAll(pageable);
    }

    public Optional<Meeting> getMeeting(Long id) {
        return meetingRepository.findById(id);
    }

    @Transactional
    public Meeting create(String subject, Member member, Integer capacity, String location, String date, String time, String content) {
        Meeting meeting = Meeting
                .builder()
                .subject(subject)
                .member(member)
                .capacity(capacity)
                .participantsCount(1)
                .location(location)
                .date(date)
                .time(time)
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

    @Transactional
    public RsData<Meeting> modify(Meeting meeting, String subject, Integer capacity,
                                  String location, String date, String time, String content) {

        meeting.update(subject, capacity, location, date, time, content);
        meetingRepository.save(meeting);

        return RsData.of("S-1", "모임 내용을 수정하였습니다.", meeting);
    }

    public RsData canModify(Member actor, Meeting meeting) {
        long actorMemberId = actor.getId();

        if (!Objects.equals(actorMemberId, meeting.getMember().getId()))
            return RsData.of("F-1", "해당 모임을 수정할 권한이 없습니다.");

        return RsData.of("S-1", "모임 수정이 가능합니다.");
    }

    public RsData checkCapacity(Integer capacity, Integer participantsCount) {

        if(capacity < participantsCount)
            return RsData.of("F-1", "모집인원이 현재 모임 참여자 수보다 적습니다.");

        return RsData.of("S-1", "모임 수정이 가능합니다.");
    }

    public List<Meeting> getListForMember(Long memberId, Long currentMemberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Member foundMember = findByMemberId(member, currentMemberId);

        if (foundMember == null) {
            return null;
        }

        return meetingRepository.findByMember(foundMember);
    }


    private Member findByMemberId(Optional<Member> member, Long memberId) {
        if (member.isPresent()) {
            Member foundMember = member.get();
            if (foundMember.getId().equals(memberId)) {
                return foundMember;
            }
        }

        return null;
    }

}
