package com.mobble.mobbleserver.application.like.command;

import com.mobble.mobbleserver.domain.like.LikeType;

import java.util.List;

public record MemberLikedTargetsResult(LikeType likeType, Long memberId, List<Long> likedTargetIds) {

    public static MemberLikedTargetsResult toDto(LikeType likeType, Long memberId, List<Long> likedTargetIds) {
        return new MemberLikedTargetsResult(likeType, memberId, likedTargetIds);
    }
}
