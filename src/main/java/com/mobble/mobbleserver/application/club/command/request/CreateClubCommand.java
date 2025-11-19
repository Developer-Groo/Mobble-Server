package com.mobble.mobbleserver.application.club.command.request;

import com.mobble.mobbleserver.domain.category.CategoryCode;
import com.mobble.mobbleserver.domain.club.AgeGroup;

public record CreateClubCommand(
        Long ownerId,
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
        Long mainImageId // Todo: 확인 필요
) {

    public static CreateClubCommand create(
            Long ownerId,
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
            Long mainImageId // Todo: 확인 필요
    ) {
        return new CreateClubCommand(
                ownerId,
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
