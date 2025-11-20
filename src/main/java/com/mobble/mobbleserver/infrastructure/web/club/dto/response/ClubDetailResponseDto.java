package com.mobble.mobbleserver.infrastructure.web.club.dto.response;

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
}
