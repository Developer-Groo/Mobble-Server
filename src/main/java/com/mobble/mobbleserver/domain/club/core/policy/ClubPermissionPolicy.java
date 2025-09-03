package com.mobble.mobbleserver.domain.club.core.policy;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.security.SecurityErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClubPermissionPolicy {

    private final ClubMember clubMember;

    /**
     * 모임장(LEADER)만 가능한 권한 검증
     */
    public void validateLeaderOnly() {
        if (clubMember.getClubMemberRole() != ClubMemberRole.LEADER) throw new DomainException(SecurityErrorCode.ACCESS_DENIED);
    }

    /**
     * 관리자(LEADER, MANAGER)만 가능한 권한 검증
     */
    public void validateLeaderOrManager() {
        if (clubMember.getClubMemberRole() != ClubMemberRole.LEADER && clubMember.getClubMemberRole() != ClubMemberRole.MANAGER) throw new DomainException(SecurityErrorCode.ACCESS_DENIED);
    }

    /**
     * 모임장 여부 판단 (예: 모임장 전용 기능 표시 활용(멤버 권한 변경 등))
     */
    public boolean isLeader() {
        return clubMember.getClubMemberRole() == ClubMemberRole.LEADER;
    }

    /**
     * 관리자 여부 판단 (예: 관리자 전용 기능 표시 활용(수정, 삭제 버튼 노출), 응답 DTO 등 조건 분기 용도)
     */
    public boolean isLeaderOrManager() {
        return clubMember.getClubMemberRole() == ClubMemberRole.LEADER || clubMember.getClubMemberRole() == ClubMemberRole.MANAGER;
    }
}
