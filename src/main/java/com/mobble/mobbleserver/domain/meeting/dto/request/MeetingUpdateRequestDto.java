package com.mobble.mobbleserver.domain.meeting.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.domain.meeting.entity.MeetingType;

import java.time.LocalDateTime;

public record MeetingUpdateRequestDto(
        String title,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime dateTime,
        String location,
        String cost,
        Integer memberLimit,
        MeetingType type
) {
}
