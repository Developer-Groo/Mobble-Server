package com.mobble.mobbleserver.domain.meeting.repository;

import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    //    List<Meeting> findMeetingsByClubId(Long clubId);
    List<Meeting> findByClubMember_Club_Id(Long clubId);
}
