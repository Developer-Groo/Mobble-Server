package com.mobble.mobbleserver.domain.like.clubLike.dto.response;

public record ClubLikeResponseDto(Long clubId, boolean isLiked, int likeCount) {

    public static ClubLikeResponseDto toDto(Long clubId, boolean isLiked, int likeCount) {
        return new ClubLikeResponseDto(clubId, isLiked, likeCount);
    }
}
