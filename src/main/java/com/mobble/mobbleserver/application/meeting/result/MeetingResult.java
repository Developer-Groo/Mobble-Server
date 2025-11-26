package com.mobble.mobbleserver.application.meeting.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.application.meetingMember.command.response.MeetingMemberInfoResult;
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

    public static List<MeetingResult> create(List<Meeting> meetings, List<Long> attendedList) {
        return meetings.stream()
                .map(meeting -> toResult(meeting, attendedList))
                .toList();
    }

    public static MeetingResult toResult(Meeting meeting, List<Long> attendedList) {
        return new MeetingResult(
                meeting.getId(),
                meeting.getClubMember().getClub().getId(),
                meeting.getTitle(),
                meeting.getSchedule().getDatetime(),
                meeting.getLocation(),
                meeting.getCost(),
                meeting.getMemberLimit(),
                meeting.getAttendeeCount(),
                meeting.getType(),
                meeting.calculateDDay(),
                toAttendedMembers(meeting),
                attendedList.contains(meeting.getId())
        );
    }

    /* ==== Private Helper ==== */
    private static List<MeetingMemberInfoResult> toAttendedMembers(Meeting meeting) {
        return meeting.getAttendedMembers().stream()
                .map(MeetingMemberInfoResult::toMeetingMemberInfo)
                .toList();
    }
}
