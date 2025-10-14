package com.mobble.mobbleserver.infrastructure.web.meeting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meeting.MeetingType;

import java.time.LocalDateTime;

public record MeetingResponseDto(
        Long meetingId,
        Long clubId,
        String title,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime dateTime,
        String location,
        String cost,
        Integer memberLimit,
        int attendeeCount,
        MeetingType type,
        int dDay
) {
    
    public static MeetingResponseDto toDto(Meeting meeting, int attendeeCount, int dDay) {
        return new MeetingResponseDto(
                meeting.getId(),
                meeting.getClubMember().getClub().getId(),
                meeting.getTitle(),
                meeting.getDatetime(),
                meeting.getLocation(),
                meeting.getCost(),
                meeting.getMemberLimit(),
                attendeeCount,
                meeting.getType(),
                dDay
        );
    }
}
