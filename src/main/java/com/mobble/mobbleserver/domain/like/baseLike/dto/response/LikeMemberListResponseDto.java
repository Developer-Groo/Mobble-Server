package com.mobble.mobbleserver.domain.like.baseLike.dto.response;

import com.mobble.mobbleserver.domain.member.entity.Member;

import java.util.List;
import java.util.function.Function;

public record LikeMemberListResponseDto(Long targetId, List<LikeMemberResponseDto> likedMembers) {

    public static <T> LikeMemberListResponseDto toDto(Long targetId, List<T> likeEntities, Function<T, Member> memberExtractor) {
        List<LikeMemberResponseDto> likedMembers = likeEntities.stream()
                .map(memberExtractor.andThen(LikeMemberResponseDto::toDto))
                .toList();

        return new LikeMemberListResponseDto(targetId, likedMembers);
    }
}
