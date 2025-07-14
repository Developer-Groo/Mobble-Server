package com.mobble.mobbleserver.domain.like.clubLike.dto.response;

public record ClubLikeResponseDto(Long clubId, boolean isLiked) {

    public static ClubLikeResponseDto toDto(Long clubId, boolean isLiked) {
        return new ClubLikeResponseDto(clubId, isLiked);
    }
}
