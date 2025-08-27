package com.mobble.mobbleserver.domain.meetingMember.dto.response;

public record MeetingAttendanceResponseDto(Long meetingId, boolean isAttending) {

    public static MeetingAttendanceResponseDto toDto(Long meetingId, boolean isAttending) {
        return new MeetingAttendanceResponseDto(meetingId, isAttending);
    }
}
