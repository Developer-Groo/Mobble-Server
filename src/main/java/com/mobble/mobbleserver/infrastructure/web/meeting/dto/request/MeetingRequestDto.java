package com.mobble.mobbleserver.infrastructure.web.meeting.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.domain.meeting.MeetingType;

import java.time.LocalDateTime;

public record MeetingRequestDto(
        String title,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime schedule,
        String location,
        String cost,
        Integer memberLimit,
        MeetingType type
) {
}
