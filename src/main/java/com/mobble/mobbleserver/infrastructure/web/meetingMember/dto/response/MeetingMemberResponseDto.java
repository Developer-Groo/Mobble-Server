package com.mobble.mobbleserver.infrastructure.web.meetingMember.dto.response;

import com.mobble.mobbleserver.domain.member.Member;

public record MeetingMemberResponseDto(Long memberId, String name, String profileImage) {

    public static MeetingMemberResponseDto toDto(Member member) {
        return new MeetingMemberResponseDto(member.getId(), member.getName(), member.getProfileImage().getUrl());
    }
}
