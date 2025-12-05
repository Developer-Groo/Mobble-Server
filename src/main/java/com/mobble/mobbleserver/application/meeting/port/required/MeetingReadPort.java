package com.mobble.mobbleserver.application.meeting.port.required;

import com.mobble.mobbleserver.domain.meeting.Meeting;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeetingReadPort {

    Optional<Meeting> findById(Long meetingId);

    List<Meeting> findMeetingsByClubId(Long clubId);

    List<Meeting> findUpcomingMeetingsByClubId(Long clubId, LocalDateTime today);

    List<Long> findMainImageIdsByClubId(Long clubId);
}
