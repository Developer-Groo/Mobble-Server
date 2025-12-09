package com.mobble.mobbleserver.application.meeting.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.application.meetingMember.result.MeetingMemberInfoResult;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meeting.MeetingType;

import java.time.LocalDateTime;
import java.util.List;

public record MeetingResult(
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
        int dDay,
        List<MeetingMemberInfoResult> attendedMembers,
        boolean isAttended
) {

    public static List<MeetingResult> create(List<Meeting> meetings, Long memberId) {
        return meetings.stream()
                .map(meeting -> toResult(meeting, memberId))
                .toList();
    }

    public static MeetingResult toResult(Meeting meeting, Long memberId) {
        return new MeetingResult(
                meeting.getId(),
                meeting.getClub().getId(),
                meeting.getTitle(),
                meeting.getSchedule().getDatetime(),
                meeting.getLocation(),
                meeting.getCost(),
                meeting.getMemberLimit(),
                meeting.getAttendeeCount(),
                meeting.getType(),
                meeting.calculateDDay(),
                toAttendedMembers(meeting),
                meeting.hasAttendee(memberId)
        );
    }

    /* ==== Private Helper ==== */
    private static List<MeetingMemberInfoResult> toAttendedMembers(Meeting meeting) {
        return meeting.getAttendedMembers().stream()
                .map(MeetingMemberInfoResult::toMeetingMemberInfo)
                .toList();
    }
}
