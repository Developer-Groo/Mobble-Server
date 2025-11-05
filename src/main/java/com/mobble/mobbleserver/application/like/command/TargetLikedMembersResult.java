package com.mobble.mobbleserver.application.like.command;

import java.util.List;

public record TargetLikedMembersResult(Long targetId, List<Long> likedMemberIds) {

    public static TargetLikedMembersResult toDto(Long targetId, List<Long> likedMemberIds) {
        return new TargetLikedMembersResult(targetId, likedMemberIds);
    }
}
