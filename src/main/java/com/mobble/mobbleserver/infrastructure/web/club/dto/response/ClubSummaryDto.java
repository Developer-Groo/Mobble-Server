package com.mobble.mobbleserver.infrastructure.web.club.dto.response;

import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.common.Location;
import com.mobble.mobbleserver.infrastructure.persistence.club.projection.ClubLikeInfoDto;

import java.util.List;

public record ClubSummaryDto(
        Long id,
        String name,
        String category,
        List<Location> groundNames,
        int headcount,
        int likeCount,
        boolean liked,
        boolean isAutoJoin
) {

    public static ClubSummaryDto toDto(
            Club club,
            List<Location> groundList,
            ClubLikeInfoDto likeInfo
    ) {
        return new ClubSummaryDto(
                club.getId(),
                club.getName(),
                "",
                groundList,
                club.getHeadCount(),
                likeInfo.likeCount(),
                likeInfo.isLiked(),
                club.isAutoJoin()
        );
    }
}
