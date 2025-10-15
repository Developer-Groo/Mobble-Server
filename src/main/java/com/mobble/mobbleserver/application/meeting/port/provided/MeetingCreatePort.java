package com.mobble.mobbleserver.application.meeting.port.provided;

import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.infrastructure.web.meeting.dto.request.MeetingRequestDto;

public interface MeetingCreatePort {

    Meeting createMeeting(Long memberId, Long clubId, MeetingRequestDto dto);
}
