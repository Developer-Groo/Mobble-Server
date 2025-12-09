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
        MeetingType type,

        int attendeeCount,
        int dDay,

        Long mainImageId,
        String mainImageUrl
) {

    public static MeetingResponseDto toDto(Meeting meeting) {
        return new MeetingResponseDto(
                meeting.getId(),
                meeting.getClub().getId(),
                toMeetingOwnerInfo(meeting.getOwner()),

                meeting.getTitle(),
                meeting.getSchedule().getDatetime(),
                meeting.getLocation(),
                meeting.getCost(),
                meeting.getMemberLimit(),
                meeting.getType(),

                meeting.getAttendeeCount(),
                meeting.calculateDDay(),

                meeting.getMainImage() != null ? meeting.getMainImage().getId() : null,
                meeting.getMainImage() != null ? meeting.getMainImage().getUrl() : null
        );
    }

    public static List<MeetingResponseDto> toDto(List<Meeting> meetings) {
        return meetings.stream()
                .map(MeetingResponseDto::toDto)
                .toList();
    }

    /* ==== Private Helper ==== */
    private static MeetingMemberInfoResult toMeetingOwnerInfo(Member owner) {
        return new MeetingMemberInfoResult(
                owner.getId(),
                owner.getName(),
                owner.getProfileImage() != null ? owner.getProfileImage().getId() : null,
                owner.getProfileImage() != null ? owner.getProfileImage().getUrl() : null
        );
    }
}
