package com.mobble.mobbleserver.application.clubMember.service;

import com.mobble.mobbleserver.application.club.error.ClubBusinessError;
import com.mobble.mobbleserver.application.club.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberQueryPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubMemberQueryService implements ClubMemberQueryPort {

    private final ClubMemberReadPort clubMemberReadPort;
    private final ClubReadPort clubReadPort;

    @Override
    public List<ClubMemberResponseDto> findClubMembers(Long clubId) {
        Club club = assertClubByClubId(clubId);

        List<ClubMember> clubMembers = clubMemberReadPort.findByClubId(club.getId());

//        return clubMembers.stream()
//                .map(ClubMemberResponseDto::toEntity)
//                .collect(Collectors.toList());

        return null;
    }

    /* ==== Private Helper ==== */
    private Club assertClubByClubId(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new BusinessException(ClubBusinessError.NOT_FOUND));
    }
}
