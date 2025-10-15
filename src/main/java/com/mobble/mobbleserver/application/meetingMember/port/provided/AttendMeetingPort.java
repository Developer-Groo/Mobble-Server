package com.mobble.mobbleserver.application.meetingMember.port.provided;

public interface AttendMeetingPort {

    AttendMeetingResult toggleAttendance(Long meetingId, Long memberId);
}
