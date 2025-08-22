package com.mobble.mobbleserver.domain.like.baseLike.dto.response;

import com.mobble.mobbleserver.domain.member.entity.Member;

public record LikeMemberResponseDto(Long memberId, String name, String profileImage) {

    public static LikeMemberResponseDto toDto(Member member) {
        return new LikeMemberResponseDto(member.getId(), member.getName(), member.getProfileImage());
    }
}
