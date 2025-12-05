package com.mobble.mobbleserver.infrastructure.web.club.dto.response;

import com.mobble.mobbleserver.application.club.result.ClubResult;
import com.mobble.mobbleserver.domain.category.CategoryCode;
import com.mobble.mobbleserver.domain.club.AgeGroup;

public record ClubDetailResponseDto(
        Long id,
        String name,
        String description,
        boolean isAutoJoin,
        AgeGroup ageGroup,
        CategoryCode categoryCode,

        String address1,
        String address2,
        String city,
        String district,
        Double latitude,
        Double longitude,

        Long mainImageId,
        String mainImageUrl,

        int memberCount,
        Long leaderId,
        String leaderName,

        int likeCount,
        boolean isLiked
) {

    public static ClubDetailResponseDto toDto(ClubResult result) {
        return new ClubDetailResponseDto(
                result.id(),
                result.name(),
                result.description(),
                result.isAutoJoin(),
                result.ageGroup(),
                result.categoryCode(),

                result.address1(),
                result.address2(),
                result.city(),
                result.district(),
                result.latitude(),
                result.longitude(),

                result.mainImageId(),
                result.mainImageUrl(),

                result.memberCount(),
                result.leaderId(),
                result.leaderName(),

                result.likeCount(),
                result.isLiked()
        );
    }
}
