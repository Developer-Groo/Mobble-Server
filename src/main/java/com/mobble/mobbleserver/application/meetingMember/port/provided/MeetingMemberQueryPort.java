package com.mobble.mobbleserver.application.meetingMember.port.provided;

import com.mobble.mobbleserver.infrastructure.web.meetingMember.dto.response.MeetingAttendanceResponseDto;

public interface MeetingMemberQueryPort {

    MeetingAttendanceResponseDto getIsAttended(Long meetingId, Long memberId);

    MeetingAttendanceResponseDto getMeetingMembers(Long meetingId, Long memberId);
}
