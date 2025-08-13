package com.mobble.mobbleserver.domain.meeting.dto.response;

import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.entity.MeetingType;

import java.time.LocalDateTime;

public record MeetingResponseDto(
        Long meetingId,
        Long clubId,
        String title,
        LocalDateTime dateTime,
        String location,
        String cost,
        Integer memberLimit,
        MeetingType type
//        String dDay //Todo d-day 추가
) {
    public static MeetingResponseDto toDto(Meeting meeting) {
        return new MeetingResponseDto(
                meeting.getId(),
                meeting.getClubMember().getClub().getId(),
                meeting.getTitle(),
                meeting.getDatetime(),
                meeting.getLocation(),
                meeting.getCost(),
                meeting.getMemberLimit(),
                meeting.getType()
//                dDay
        );
    }
}
