package com.mobble.mobbleserver.domain.club.core.dto.response;

import com.mobble.mobbleserver.domain.club.ageGroup.entity.AgeGroupType;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.club.core.repository.dto.ClubLikeInfoDto;

import java.util.List;

public record ClubResponseDto(
        Long id,
        String leader,
        String name,
        String category,
        String ground,
        String address,
        int headcount,
        List<AgeGroupType> ageGroup,
//        String profileImage,
//        List<String> infoImage,
        int likeCount,
        boolean liked,
        boolean isAutoJoin
) {

    public static ClubResponseDto toDto(
            Club club,
            String leaderName,
            List<AgeGroupType> ageGroup,
            ClubLikeInfoDto likeInfo
    ){
        return new ClubResponseDto(
                club.getId(),
                leaderName,
                club.getName(),
                club.getClubCategory().getName(),
                club.getGround(),
                club.getAddress(),
                club.getHeadCount(),
                ageGroup,
                likeInfo.likeCount(),
                likeInfo.isLiked(),
                club.isAutoJoin()
        );
    }
}
