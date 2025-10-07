package com.mobble.mobbleserver.domain.club.core.dto.response;

import com.mobble.mobbleserver.domain.adress.dto.response.AddressResponseDto;
import com.mobble.mobbleserver.domain.adress.entity.Address;
import com.mobble.mobbleserver.domain.club.ageGroup.entity.AgeGroupType;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.club.core.repository.dto.ClubLikeInfoDto;
import com.mobble.mobbleserver.domain.ground.dto.response.GroundResponseDto;

import java.util.List;

public record ClubResponseDto(
        Long id,
        String leader,
        String name,
        String category,
        List<GroundResponseDto> groundList,
        AddressResponseDto address,
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
            Address address,
            List<AgeGroupType> ageGroup,
            List<GroundResponseDto> groundList,
            ClubLikeInfoDto likeInfo
    ){
        return new ClubResponseDto(
                club.getId(),
                leaderName,
                club.getName(),
                club.getClubCategory().getName(),
                groundList,
                AddressResponseDto.toDto(address),
                club.getHeadCount(),
                ageGroup,
                likeInfo.likeCount(),
                likeInfo.isLiked(),
                club.isAutoJoin()
        );
    }
}
