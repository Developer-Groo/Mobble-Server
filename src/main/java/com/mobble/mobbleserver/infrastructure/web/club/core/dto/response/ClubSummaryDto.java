package com.mobble.mobbleserver.infrastructure.web.club.core.dto.response;

import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.ground.Ground;
import com.mobble.mobbleserver.infrastructure.persistence.club.core.projection.ClubLikeInfoDto;

import java.util.List;

public record ClubSummaryDto(
        Long id,
        String name,
        String category,
        List<String> groundNames,
        int headcount,
        int likeCount,
        boolean liked,
        boolean isAutoJoin
) {

    public static ClubSummaryDto toDto(
            Club club,
            List<Ground> groundList,
            ClubLikeInfoDto likeInfo
    ) {
        List<String> groundNames = groundList.stream()
                .map(g -> buildGroundName(g))
                .toList();

        return new ClubSummaryDto(
                club.getId(),
                club.getName(),
                club.getClubCategory().getName(),
                groundNames,
                club.getHeadCount(),
                likeInfo.likeCount(),
                likeInfo.isLiked(),
                club.isAutoJoin()
        );
    }

    private static String buildGroundName(Ground g) {
        return g.getEubmyeondong() != null ? g.getEubmyeondong() : "";
    }
}
