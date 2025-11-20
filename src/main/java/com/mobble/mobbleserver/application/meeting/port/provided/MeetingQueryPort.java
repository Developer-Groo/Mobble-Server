package com.mobble.mobbleserver.application.meeting.port.provided;

import com.mobble.mobbleserver.domain.meeting.Meeting;

import java.util.List;

public interface MeetingQueryPort {

    List<Meeting> findMeetingsByClubId(Long clubId);
}
