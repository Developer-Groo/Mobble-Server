package com.mobble.mobbleserver.infrastructure.web.like.dto.response;

import com.mobble.mobbleserver.domain.member.Member;

public record LikeMemberResponseDto(Long memberId, String name, String profileImage) {

    public static LikeMemberResponseDto toDto(Member member) {
        return new LikeMemberResponseDto(member.getId(), member.getName(), member.getProfileImage());
    }
}
