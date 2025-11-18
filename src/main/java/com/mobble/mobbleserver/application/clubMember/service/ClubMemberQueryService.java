package com.mobble.mobbleserver.application.clubMember.service;

import com.mobble.mobbleserver.application.club.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberQueryPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubMemberQueryService implements ClubMemberQueryPort {

    private final ClubMemberReadPort clubMemberReadPort;
    private final ClubReadPort clubReadPort;

    @Override
    public List<ClubMemberResponseDto> findClubMembers(Long clubId) {
        Club club = findClubByClubIdOrThrow(clubId);

        List<ClubMember> clubMembers = clubMemberReadPort.findByClubId(clubId);

        return clubMembers.stream()
                .map(ClubMemberResponseDto::toEntity)
                .collect(Collectors.toList());
    }

    private Club findClubByClubIdOrThrow(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new DomainException((ClubErrorCode.NOT_FOUND)));
    }

}
