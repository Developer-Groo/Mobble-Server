package com.mobble.mobbleserver.application.like.core.command;

import java.util.List;

public record TargetLikedMembersResult(Long targetId, List<LikedMemberInfoResult> likedMembers) {

    public static TargetLikedMembersResult toDto(Long targetId, List<LikedMemberInfoResult> likedMembers) {
        return new TargetLikedMembersResult(targetId, likedMembers);
    }
}
