package com.mobble.mobbleserver.infrastructure.web.like.core.dto.response;

import com.mobble.mobbleserver.domain.member.Member;

public record LikedMemberInfoResult(Long memberId, String name, String profileImage) {

    public static LikedMemberInfoResult toDto(Member member) {
        return new LikedMemberInfoResult(member.getId(), member.getName(), member.getProfileImage());
    }
}
