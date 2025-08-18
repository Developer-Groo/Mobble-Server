package com.mobble.mobbleserver.domain.clubMember.validator;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.repository.ClubMemberRepository;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberValidationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClubMemberValidator {

    private final ClubMemberRepository clubMemberRepository;

    public ClubMember findClubMemberByClubIdAndMemberIdOrThrow(Long clubId, Long memberId) {
        return clubMemberRepository.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberValidationErrorCode.CLUB_MEMBER_NOT_FOUND));
    }

    public List<ClubMember> findAllClubMemberByMemberId(Long memberId) {
        return clubMemberRepository.findAllClubMemberByMemberId(memberId);
    }
}
