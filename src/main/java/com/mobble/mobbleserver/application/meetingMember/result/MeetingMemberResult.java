package com.mobble.mobbleserver.application.meetingMember.result;

import com.mobble.mobbleserver.domain.member.Member;

import java.util.List;

public record MeetingMemberResult(Long meetingId, List<Member> attendedMembers) {

    public static MeetingMemberResult create(Long meetingId, List<Member> attendedMembers) {
        return new MeetingMemberResult(meetingId, attendedMembers);
    }
}
