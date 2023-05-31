package com.ll.FlexGym.domain.Meeting.service;

import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;

    public List<Meeting> getList() {
        return this.meetingRepository.findAll();
    }

    public Optional<Meeting> getMeeting(Integer id) {
        return meetingRepository.findById(id);
    }

    public void create(String subject, Integer capacity, String location, String dateTime, String content) {
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
