package com.mobble.mobbleserver.infrastructure.web.meetingMember.dto.response;

import com.mobble.mobbleserver.application.meetingMember.result.MeetingMemberInfoResult;
import com.mobble.mobbleserver.domain.member.Member;

import java.util.List;

public record MeetingMemberListResponseDto(
        Long meetingId,
        List<MeetingMemberInfoResult> attendedMembers
) {

    public static MeetingMemberListResponseDto toDto(Long meetingId, List<Member> attendedMembers) {
        return new MeetingMemberListResponseDto(meetingId, toAttendedMembers(attendedMembers));
    }

    private static List<MeetingMemberInfoResult> toAttendedMembers(List<Member> attendedMembers) {
        return attendedMembers.stream()
                .map(MeetingMemberInfoResult::toMeetingMemberInfo)
                .toList();
    }
}
