package com.mobble.mobbleserver.domain.club.club.dto.response;

import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.club.club.repository.dto.ClubLikeInfoDto;
import com.mobble.mobbleserver.domain.club.clubAgeGroup.entity.ClubAgeGroupType;

import java.util.List;

public record ClubResponseDto(
        Long id,
        String leader,
        String name,
        String category,
        String ground,
        String address,
        int headcount,
        List<ClubAgeGroupType> ageGroup,
//        String profileImage,
//        List<String> infoImage,
        int likeCount,
        boolean liked,
        boolean isAutoJoin
) {

    public static ClubResponseDto toDto(
            Club club,
            String leaderName,
            List<ClubAgeGroupType> ageGroup,
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
