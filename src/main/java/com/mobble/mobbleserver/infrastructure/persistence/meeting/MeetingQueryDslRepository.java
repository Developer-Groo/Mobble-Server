package com.mobble.mobbleserver.infrastructure.persistence.meeting;

import com.mobble.mobbleserver.domain.meeting.Meeting;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingQueryDslRepository {

    List<Meeting> findUpcomingMeetings(Long clubId, LocalDateTime dateTime);
}
