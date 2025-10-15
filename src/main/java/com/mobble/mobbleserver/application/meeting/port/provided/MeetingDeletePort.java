package com.mobble.mobbleserver.application.meeting.port.provided;

public interface MeetingDeletePort {

    void deleteMeeting(Long memberId, Long clubId, Long meetingId);
}
