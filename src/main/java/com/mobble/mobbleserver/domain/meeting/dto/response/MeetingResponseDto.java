package com.mobble.mobbleserver.domain.meeting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.entity.MeetingType;

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
        MeetingType type
//        String dDay //Todo d-day 추가
) {
    
    public static MeetingResponseDto toDto(Meeting meeting, int attendeeCount) {
        return new MeetingResponseDto(
                meeting.getId(),
                meeting.getClubMember().getClub().getId(),
                meeting.getTitle(),
                meeting.getDatetime(),
                meeting.getLocation(),
                meeting.getCost(),
                meeting.getMemberLimit(),
                attendeeCount,
                meeting.getType()
//                dDay
        );
    }
}
