package com.mobble.mobbleserver.infrastructure.web.meeting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meeting.MeetingType;

import java.time.LocalDateTime;
import java.util.List;

public record MeetingResponseDto(
        Long meetingId,
        Long clubId,
        String title,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime schedule,
        String location,
        String cost,
        Integer memberLimit,
        int attendeeCount,
        MeetingType type,
        int dDay
) {

    public static MeetingResponseDto toDto(Meeting meeting) {
        return new MeetingResponseDto(
                meeting.getId(),
                meeting.getClubMember().getClub().getId(),
                meeting.getTitle(),
                meeting.getSchedule().getDatetime(),
                meeting.getLocation(),
                meeting.getCost(),
                meeting.getMemberLimit(),
                meeting.getAttendeeCount(),
                meeting.getType(),
                meeting.calculateDDay()
        );
    }

    public static List<MeetingResponseDto> toDto(List<Meeting> meetings) {
        return meetings.stream()
                .map(MeetingResponseDto::toDto)
                .toList();
    }
}
