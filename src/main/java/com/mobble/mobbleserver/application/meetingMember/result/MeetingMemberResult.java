package com.mobble.mobbleserver.application.meetingMember.result;

import com.mobble.mobbleserver.domain.member.Member;

import java.util.List;

public record MeetingMemberResult(Long meetingId, List<MeetingMemberInfoResult> attendedMembers) {

    public static MeetingMemberResult create(Long meetingId, List<Member> attendedMembers) {
        return new MeetingMemberResult(meetingId, toAttendedMembers(attendedMembers));
    }

    private static List<MeetingMemberInfoResult> toAttendedMembers(List<Member> attendedMembers) {
        return attendedMembers.stream()
                .map(MeetingMemberInfoResult::toMeetingMemberInfo)
                .toList();
    }
}
