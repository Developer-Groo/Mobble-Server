package com.mobble.mobbleserver.infrastructure.web.like.core.dto.response;

import com.mobble.mobbleserver.domain.like.core.AbstractLike;

import java.util.List;

public record LikeMemberListResponseDto(Long targetId, List<LikeMemberResponseDto> likedMembers) {

    public static LikeMemberListResponseDto toDto(List<? extends AbstractLike> likes, LikeMemberMapper mapper) {
        if (likes.isEmpty()) {
            return new LikeMemberListResponseDto(null, List.of());
        }

        Long targetId = likes.get(0).getTargetId();

        List<LikeMemberResponseDto> likedMembers = likes.stream()
                .map(AbstractLike::getMemberId)
                .map(mapper::toDto)
                .toList();

        return new LikeMemberListResponseDto(targetId, likedMembers);
    }
}
