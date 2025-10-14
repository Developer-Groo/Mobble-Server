package com.mobble.mobbleserver.infrastructure.persistence.meeting;

import com.mobble.mobbleserver.infrastructure.web.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaMeetingRepository extends JpaRepository<Meeting, Long> {
    //    List<Meeting> findMeetingsByClubId(Long clubId);
    List<Meeting> findByClubMember_Club_Id(Long clubId);
}
