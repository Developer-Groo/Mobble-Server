package com.mobble.mobbleserver.refactor.club.core.repository.dto;

public record ClubLikeInfoDto(int likeCount, boolean isLiked) {

    public static ClubLikeInfoDto toDto(int likeCount, boolean isLiked){
        return new ClubLikeInfoDto(likeCount, isLiked);
    }
}
