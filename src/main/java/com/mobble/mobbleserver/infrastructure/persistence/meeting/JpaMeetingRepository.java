package com.mobble.mobbleserver.infrastructure.persistence.meeting;

import com.mobble.mobbleserver.domain.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaMeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByClubIdOrderBySchedule_DatetimeAsc(Long clubId);

    List<Meeting> findByClubIdAndSchedule_DatetimeGreaterThanEqualOrderBySchedule_DatetimeAsc(Long clubId, LocalDateTime dateTime);

    List<Long> findMainImageIdsByClubId(Long clubId);
}
