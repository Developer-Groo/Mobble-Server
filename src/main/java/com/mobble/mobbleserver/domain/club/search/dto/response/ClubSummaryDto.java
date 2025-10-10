package com.mobble.mobbleserver.domain.club.search.dto.response;

import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.club.core.repository.dto.ClubLikeInfoDto;
import com.mobble.mobbleserver.domain.ground.entity.Ground;

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
