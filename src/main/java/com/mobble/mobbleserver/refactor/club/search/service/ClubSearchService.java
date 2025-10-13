package com.mobble.mobbleserver.refactor.club.search.service;

import com.mobble.mobbleserver.refactor.club.clubGround.repository.ClubGroundRepository;
import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.club.core.repository.ClubRepository;
import com.mobble.mobbleserver.refactor.club.core.repository.dto.ClubLikeInfoDto;
import com.mobble.mobbleserver.refactor.club.search.dto.request.ClubSearchRequestDto;
import com.mobble.mobbleserver.refactor.club.search.dto.response.ClubSummaryDto;
import com.mobble.mobbleserver.refactor.club.search.repository.ClubSearchRepository;
import com.mobble.mobbleserver.refactor.ground.entity.Ground;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubSearchService {

    private final ClubSearchRepository clubSearchRepository;
    private final ClubRepository clubRepository;
    private final ClubGroundRepository clubGroundRepository;

    public List<ClubSummaryDto> searchClubs(ClubSearchRequestDto dto, Long memberId) {
        List<Club> clubs = clubSearchRepository.searchClubs(dto);

        return clubs.stream()
                .map(club -> {
                    List<Ground> groundList = clubGroundRepository.findGroundsByClubId(club.getId());
                    ClubLikeInfoDto likeInfo = clubRepository.findLikeInfoByClubIdAndMemberId(club.getId(), memberId);
                    return ClubSummaryDto.toDto(club, groundList, likeInfo);
                })
                .toList();
    }
}
