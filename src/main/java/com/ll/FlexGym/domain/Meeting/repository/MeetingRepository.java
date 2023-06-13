package com.ll.FlexGym.domain.Meeting.repository;

import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Page<Meeting> findAll(Pageable pageable);
}
