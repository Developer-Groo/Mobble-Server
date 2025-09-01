package com.mobble.mobbleserver.domain.club.core.validator;

import com.mobble.mobbleserver.account.auth.principal.AuthMemberExtractor;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubRolePermissionChecker {

    private final AuthMemberExtractor authMemberExtractor;
    private final ClubMemberValidator clubMemberValidator;

    public boolean isLeader(Long clubId) {
        Long memberId = authMemberExtractor.getCurrentMemberId();

        ClubMemberRole role = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId).getClubMemberRole();

        return role == ClubMemberRole.LEADER;
    }

    public boolean isLeaderOrManager(Long clubId) {
        Long memberId = authMemberExtractor.getCurrentMemberId();

        ClubMemberRole role = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId).getClubMemberRole();

        return role == ClubMemberRole.LEADER || role == ClubMemberRole.MANAGER;
    }
}
