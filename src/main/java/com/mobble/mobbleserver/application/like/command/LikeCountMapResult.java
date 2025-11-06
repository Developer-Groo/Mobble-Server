package com.mobble.mobbleserver.application.like.command;

import com.mobble.mobbleserver.domain.like.LikeType;

import java.util.Map;

public record LikeCountMapResult(LikeType likeType, Map<Long, Long> countsByTargetId) {

    public static LikeCountMapResult toDto(LikeType likeType, Map<Long, Long> countsByTargetId) {
        return new LikeCountMapResult(likeType, countsByTargetId);
    }
}
