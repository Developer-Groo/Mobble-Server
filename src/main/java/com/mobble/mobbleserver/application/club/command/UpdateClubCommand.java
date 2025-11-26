package com.mobble.mobbleserver.application.club.command;

import com.mobble.mobbleserver.domain.category.CategoryCode;
import com.mobble.mobbleserver.domain.club.AgeGroup;

public record UpdateClubCommand(
        Long clubId,
        Long leaderId,

        String name,
        String description,
        boolean isAutoJoin,

        CategoryCode categoryCode,
        AgeGroup ageGroup,

        String address1,
        String address2,
        String city,
        String district,
        Double latitude,
        Double longitude,

        Long mainImageId
) {

    public static UpdateClubCommand create(
            Long clubId,
            Long leaderId,
            String name,
            String description,
            boolean isAutoJoin,
            CategoryCode categoryCode,
            AgeGroup ageGroup,
            String address1,
            String address2,
            String city,
            String district,
            Double latitude,
            Double longitude,
            Long mainImageId
    ) {
        return new UpdateClubCommand(
                clubId,
                leaderId,
                name,
                description,
                isAutoJoin,
                categoryCode,
                ageGroup,
                address1,
                address2,
                city,
                district,
                latitude,
                longitude,
                mainImageId
        );
    }
}
