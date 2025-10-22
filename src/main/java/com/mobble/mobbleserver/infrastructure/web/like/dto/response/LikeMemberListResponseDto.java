package com.mobble.mobbleserver.infrastructure.web.like.dto.response;

import com.mobble.mobbleserver.domain.like.baseLike.BaseLike;

import java.util.List;

public record LikeMemberListResponseDto(Long targetId, List<LikeMemberResponseDto> likedMembers) {

    public static <T> LikeMemberListResponseDto toDto(List<? extends BaseLike> likes) {
        if (likes.isEmpty()) {
            return new LikeMemberListResponseDto(null, List.of());
        }

        Long targetId = likes.get(0).getTargetId();

        List<LikeMemberResponseDto> likedMembers = likes.stream()
                .map(BaseLike::getMember)
                .map(LikeMemberResponseDto::toDto)
                .toList();

        return new LikeMemberListResponseDto(targetId, likedMembers);
    }
}
