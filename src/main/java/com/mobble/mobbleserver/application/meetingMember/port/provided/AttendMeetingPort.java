package com.mobble.mobbleserver.application.meetingMember.port.provided;

public interface AttendMeetingPort {

    void toggleAttend(Long meetingId, Long memberId);
}
