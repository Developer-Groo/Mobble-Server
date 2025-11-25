package com.mobble.mobbleserver.application.meetingMember.command.response;

import com.mobble.mobbleserver.domain.member.Member;

public record MeetingMemberInfoResult(Long memberId, String name, String profileImage) {

    public static MeetingMemberInfoResult toMemberInfo(Member member) {
        return new MeetingMemberInfoResult(
                member.getId(),
                member.getName(),
                member.getProfileImage()
        );
    }
}
