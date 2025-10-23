package com.mobble.mobbleserver.infrastructure.persistence.club.core.projection;

public record ClubLikeInfoDto(int likeCount, boolean isLiked) {

    public static ClubLikeInfoDto toDto(int likeCount, boolean isLiked){
        return new ClubLikeInfoDto(likeCount, isLiked);
    }
}
