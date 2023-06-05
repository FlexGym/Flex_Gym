package com.ll.FlexGym.domain.Meeting.service;

import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Meeting.repository.MeetingRepository;
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
    public void create(String subject, Integer capacity, String location, LocalDateTime dateTime, String content) {
        Meeting meeting = Meeting
                .builder()
                .subject(subject)
                .capacity(capacity)
                .location(location)
                .dateTime(dateTime)
                .content(content)
                .build();

        meetingRepository.save(meeting);
    }
}
