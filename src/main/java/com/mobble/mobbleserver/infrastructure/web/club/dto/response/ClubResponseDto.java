package com.mobble.mobbleserver.infrastructure.web.club.dto.response;

import com.mobble.mobbleserver.domain.category.CategoryCode;
import com.mobble.mobbleserver.domain.club.AgeGroup;
import com.mobble.mobbleserver.domain.club.Club;

public record ClubResponseDto(
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
        String leaderName
) {

    public static ClubResponseDto toDto(Club club) {
        return new ClubResponseDto(
                club.getId(),
                club.getName(),
                club.getDescription(),
                club.isAutoJoin(),
                club.getAgeGroup(),
                club.getCategory().getCode(),

                club.getLocation().getAddress1(),
                club.getLocation().getAddress2(),
                club.getLocation().getCity(),
                club.getLocation().getDistrict(),
                club.getLocation().getLatitude(),
                club.getLocation().getLongitude(),

                club.getMainImage().getId(),
                club.getMainImage().getUrl(),

                club.getMemberCount(),
                club.getLeader().getId(),
                club.getLeader().getName()
        );
    }
}
