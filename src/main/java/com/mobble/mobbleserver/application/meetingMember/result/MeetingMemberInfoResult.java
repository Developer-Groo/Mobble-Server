package com.mobble.mobbleserver.application.meetingMember.result;

import com.mobble.mobbleserver.domain.member.Member;

public record MeetingMemberInfoResult(Long memberId, String name, String profileImage) {

    public static MeetingMemberInfoResult toMeetingMemberInfo(Member member) {
        return new MeetingMemberInfoResult(
                member.getId(),
                member.getName(),
                member.getProfileImage()
        );
    }
}
