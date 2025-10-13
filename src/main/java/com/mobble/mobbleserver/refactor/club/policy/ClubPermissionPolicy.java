package com.mobble.mobbleserver.refactor.club.policy;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.security.SecurityErrorCode;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMemberRole;

public final class ClubPermissionPolicy {

    private ClubPermissionPolicy() {}

    public static void validateLeaderOnlyOrThrow(ClubMember clubMember) {
        if (clubMember.getClubMemberRole() != ClubMemberRole.LEADER) throw new DomainException(SecurityErrorCode.ACCESS_DENIED);
    }

    public static void validateLeaderOrManagerOrThrow(ClubMember clubMember) {
        if (clubMember.getClubMemberRole() != ClubMemberRole.LEADER && clubMember.getClubMemberRole() != ClubMemberRole.MANAGER) throw new DomainException(SecurityErrorCode.ACCESS_DENIED);
    }

    public static boolean isLeader(ClubMember clubMember) {
        return clubMember.getClubMemberRole() == ClubMemberRole.LEADER;
    }

    public static boolean isLeaderOrManager(ClubMember clubMember) {
        return clubMember.getClubMemberRole() == ClubMemberRole.LEADER || clubMember.getClubMemberRole() == ClubMemberRole.MANAGER;
    }
}
