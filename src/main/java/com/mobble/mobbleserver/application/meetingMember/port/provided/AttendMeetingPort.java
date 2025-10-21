package com.mobble.mobbleserver.application.meetingMember.port.provided;

public interface AttendMeetingPort {

    void attendMeeting(Long meetingId, Long memberId);
}
