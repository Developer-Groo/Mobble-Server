package com.mobble.mobbleserver.infrastructure.persistence.meeting;

import com.mobble.mobbleserver.domain.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaMeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByClubMember_Club_Id(Long clubId);

    List<Meeting> findByClubIdAndDatetimeGreaterThanEqualOrderByDatetimeAsc(Long clubId, LocalDateTime dateTime);
}
