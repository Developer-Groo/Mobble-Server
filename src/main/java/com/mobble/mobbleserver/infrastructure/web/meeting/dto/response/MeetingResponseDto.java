package com.mobble.mobbleserver.infrastructure.web.meeting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meeting.MeetingType;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    public static MeetingResponseDto toDto(Meeting meeting) {

        int attendeeCount = (meeting.getMeetingMembers() == null) ?
                0 : meeting.getMeetingMembers().size();

        int dDay = calculateDDay(meeting.getDatetime());

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

    public static List<MeetingResponseDto> listToDto(List<Meeting> meetings) {
        return meetings.stream()
                .map(MeetingResponseDto::toDto)
                .toList();
    }

    private static int calculateDDay(LocalDateTime meetingDateTime) {
        LocalDate today = LocalDate.now();
        LocalDate meetingDate = meetingDateTime.toLocalDate();

        return (int) Duration.between(today.atStartOfDay(), meetingDate.atStartOfDay()).toDays();
    }
}
