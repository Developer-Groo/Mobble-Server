package com.mobble.mobbleserver.infrastructure.persistence.meeting;

import com.mobble.mobbleserver.domain.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaMeetingRepository extends JpaRepository<Meeting, Long>, MeetingQueryDslRepository {

    List<Meeting> findByClubIdOrderBySchedule_DatetimeAsc(Long clubId);

    List<Long> findMainImageIdsByClubId(Long clubId);
}
