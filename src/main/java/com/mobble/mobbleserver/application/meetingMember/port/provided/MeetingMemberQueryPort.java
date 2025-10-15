package com.mobble.mobbleserver.application.meetingMember.port.provided;

import com.mobble.mobbleserver.domain.meetingMember.MeetingMember;
import com.mobble.mobbleserver.infrastructure.web.meetingMember.dto.response.MeetingAttendanceResponseDto;

import java.util.List;

public interface MeetingMemberQueryPort {

    MeetingAttendanceResponseDto getIsAttended(Long meetingId, Long memberId);

    List<MeetingMember> getMeetingMembers(Long meetingId);
}
