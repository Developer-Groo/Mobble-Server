package com.mobble.mobbleserver.application.meetingMember.port.provided;

import com.mobble.mobbleserver.infrastructure.web.meetingMember.dto.response.MeetingAttendanceResponseDto;
import com.mobble.mobbleserver.infrastructure.web.meetingMember.dto.response.MeetingMemberListResponseDto;

public interface MeetingMemberQueryPort {

    MeetingAttendanceResponseDto getIsAttended(Long meetingId, Long memberId);

    MeetingMemberListResponseDto getMeetingMembers(Long meetingId, Long memberId);
}
