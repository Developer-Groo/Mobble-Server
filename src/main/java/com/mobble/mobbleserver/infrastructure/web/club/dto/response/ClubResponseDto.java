package com.mobble.mobbleserver.infrastructure.web.club.dto.response;

import com.mobble.mobbleserver.domain.club.AgeGroup;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.infrastructure.persistence.club.projection.ClubLikeInfoDto;

import java.util.List;

public record ClubResponseDto(
        Long id,
        String leader,
        String name,
        String category,
        int headcount,
        List<AgeGroup> ageGroup,
//        String profileImage,
//        List<String> infoImage,
        int likeCount,
        boolean liked,
        boolean isAutoJoin
) {

    public static ClubResponseDto toDto(
            Club club,
            String leaderName,
            List<AgeGroup> ageGroup,
//            List<GroundResponseDto> groundList,
            ClubLikeInfoDto likeInfo
    ){
        return new ClubResponseDto(
                club.getId(),
                leaderName,
                club.getName(),
                "",
//                groundList,
                club.getHeadCount(),
                ageGroup,
                likeInfo.likeCount(),
                likeInfo.isLiked(),
                club.isAutoJoin()
        );
    }
}
