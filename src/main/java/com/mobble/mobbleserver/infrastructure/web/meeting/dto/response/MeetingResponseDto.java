package com.mobble.mobbleserver.infrastructure.web.meeting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.application.meetingMember.result.MeetingMemberInfoResult;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meeting.MeetingType;
import com.mobble.mobbleserver.domain.member.Member;

import java.time.LocalDateTime;
import java.util.List;

public record MeetingResponseDto(
        Long meetingId,
        Long clubId,
        MeetingMemberInfoResult ownerInfo,
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
                meeting.getClub().getId(),
                toMeetingMemberInfo(meeting.getOwner()),
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

    /* ==== Private Helper ==== */
    private static MeetingMemberInfoResult toMeetingMemberInfo(Member owner) {
        return new MeetingMemberInfoResult(
                owner.getId(),
                owner.getName(),
                owner.getProfileImage().getUrl()
        );
    }
}
