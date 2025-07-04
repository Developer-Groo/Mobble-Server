package com.mobble.mobbleserver.domain.like.articleLike.dto.response;

import com.mobble.mobbleserver.domain.member.entity.Member;

public record ArticleLikeMemberResponseDto(Long memberId, String name, String profileImage) {
    public static ArticleLikeMemberResponseDto toDto(Member member) {
        return new ArticleLikeMemberResponseDto(member.getId(), member.getName(), member.getProfileImage());
    }
}
