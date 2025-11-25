package com.mobble.mobbleserver.application.meeting.port.provided;

import com.mobble.mobbleserver.application.meeting.command.response.MeetingResult;

import java.util.List;

public interface MeetingQueryPort {

    List<MeetingResult> findMeetings(Long memberId, Long clubId);

    List<MeetingResult> findUpcomingMeetings(Long memberId, Long clubId);

}
