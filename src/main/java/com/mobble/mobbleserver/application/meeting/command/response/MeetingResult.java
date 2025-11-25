package com.mobble.mobbleserver.application.meeting.command.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meeting.MeetingType;
import com.mobble.mobbleserver.domain.member.Member;

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
        List<MemberInfo> attendedMembers,
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

    private static List<MemberInfo> toAttendedMembers(Meeting meeting) {
        return meeting.getAttendedMembers().stream()
                .map(MemberInfo::toMemberInfo)
                .toList();
    }

    private record MemberInfo(Long memberId, String name, String profileImage) {

        public static MemberInfo toMemberInfo(Member member) {
            return new MemberInfo(
                    member.getId(),
                    member.getName(),
                    member.getProfileImage()
            );
        }
    }
}
