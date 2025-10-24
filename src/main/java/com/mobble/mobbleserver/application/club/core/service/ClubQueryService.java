package com.mobble.mobbleserver.application.club.core.service;

import com.mobble.mobbleserver.application.club.core.port.provided.ClubQueryPort;
import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.infrastructure.persistence.club.core.projection.ClubLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.request.ClubSearchRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.response.ClubResponseDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.response.ClubSummaryDto;
import com.mobble.mobbleserver.domain.address.Address;
import com.mobble.mobbleserver.refactor.club.ageGroup.entity.AgeGroup;
import com.mobble.mobbleserver.refactor.club.ageGroup.entity.AgeGroupType;
import com.mobble.mobbleserver.refactor.club.ageGroup.repository.AgeGroupRepository;
import com.mobble.mobbleserver.refactor.club.clubGround.repository.ClubGroundRepository;
import com.mobble.mobbleserver.refactor.ground.dto.response.GroundResponseDto;
import com.mobble.mobbleserver.refactor.ground.entity.Ground;
import com.mobble.mobbleserver.refactor.ground.repository.GroundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubQueryService implements ClubQueryPort {

    private final ClubReadPort clubReadPort;
    private final MemberReadPort memberReadPort;
    private final ClubMemberReadPort clubMemberReadPort;

    private final AgeGroupRepository ageGroupRepository;
    private final GroundRepository groundRepository;
    private final ClubGroundRepository clubGroundRepository;

    @Override
    public ClubResponseDto findClubById(Long clubId, Long memberId) {
        Club club = findClubByClubIdOrThrow(clubId);
        ClubMember leader = clubMemberReadPort
                .findByClubIdAndClubMemberRole(clubId, ClubMemberRole.LEADER).get();

        String leaderName = leader.getMember().getName();
        Member member = findMemberByMemberIdOrThrow(memberId);

        return buildClubResponse(club, member, leaderName);
    }

    @Override
    public List<ClubSummaryDto> searchClubs(ClubSearchRequestDto dto, Long memberId) {
        List<Club> clubs = clubReadPort.searchClubs(dto);

        return clubs.stream()
                .map(club -> {
                    List<Ground> groundList = clubGroundRepository.findGroundsByClubId(club.getId());
                    ClubLikeInfoDto likeInfo = clubReadPort.findLikeInfoByClubIdAndMemberId(club.getId(), memberId);
                    return ClubSummaryDto.toDto(club, groundList, likeInfo);
                })
                .toList();
    }

    private Club findClubByClubIdOrThrow(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new DomainException((ClubErrorCode.NOT_FOUND)));
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    private ClubResponseDto buildClubResponse(Club club, Member member, String leaderName) {
        List<AgeGroupType> ageGroupList = ageGroupRepository.findByClubId(club.getId()).stream()
                .map(AgeGroup::getAgeGroupType)
                .toList();

        List<Long> groundCodes = clubGroundRepository.findByClubId(club.getId())
                .stream()
                .map(cg -> cg.getGround().getCode())
                .collect(Collectors.toList());

        List<GroundResponseDto> groundList = groundRepository.findAllByCodeIn(groundCodes)
                .stream()
                .map(GroundResponseDto::toDto)
                .toList();

        Address address = club.getAddress();
        ClubLikeInfoDto likeInfo = clubReadPort.findLikeInfoByClubIdAndMemberId(club.getId(), member.getId());

        return ClubResponseDto.toDto(club, leaderName, address, ageGroupList, groundList, likeInfo);
    }
}
