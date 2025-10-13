package com.mobble.mobbleserver.refactor.like.baseLike.dto.response;

import com.mobble.mobbleserver.refactor.member.entity.Member;

public record LikeMemberResponseDto(Long memberId, String name, String profileImage) {

    public static LikeMemberResponseDto toDto(Member member) {
        return new LikeMemberResponseDto(member.getId(), member.getName(), member.getProfileImage());
    }
}
