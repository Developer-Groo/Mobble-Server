package com.mobble.mobbleserver.application.meeting.port.provided;

import com.mobble.mobbleserver.application.meeting.command.response.MeetingResult;

import java.util.List;

public interface MeetingQueryPort {

    List<MeetingResult> findMeetingsByClubId(Long memberId, Long clubId);

    List<MeetingResult> findUpcomingMeetingsByClubId(Long memberId, Long clubId);

}
