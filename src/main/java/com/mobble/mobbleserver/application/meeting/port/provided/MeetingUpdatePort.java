package com.mobble.mobbleserver.application.meeting.port.provided;

import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.infrastructure.web.meeting.dto.request.MeetingUpdateRequestDto;

public interface MeetingUpdatePort {

    Meeting updateMeeting(
            Long memberId,
            Long clubId,
            Long meetingId,
            MeetingUpdateRequestDto dto
    );
}
