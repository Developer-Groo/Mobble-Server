package com.mobble.mobbleserver.application.club.result;

import com.mobble.mobbleserver.domain.category.CategoryCode;
import com.mobble.mobbleserver.domain.club.AgeGroup;
import com.mobble.mobbleserver.domain.club.Club;

import java.util.List;

public record ClubResult(
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

    public static ClubResult create(Club club, int likeCount, List<Long> isLikedList) {
        return new ClubResult(
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

                club.getMainImage() != null ? club.getMainImage().getId() : null,
                club.getMainImage() != null ? club.getMainImage().getUrl() : null,

                club.getMemberCount(),
                club.getLeader().getId(),
                club.getLeader().getName(),

                likeCount,
                isLikedList.contains(club.getId())
        );
    }
}
