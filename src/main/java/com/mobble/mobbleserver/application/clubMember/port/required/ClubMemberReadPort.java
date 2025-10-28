package com.mobble.mobbleserver.application.clubMember.port.required;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;

import java.util.List;
import java.util.Optional;

public interface ClubMemberReadPort {

    Optional<ClubMember> findClubMemberByClubIdAndMemberId(Long clubId, Long memberId);

    long countByClubIdAndJoinStatus(Long id, JoinStatus joinStatus);

    List<ClubMemberRole> findDistinctRolesByMemberIdAndRoleIn(Long id, List<ClubMemberRole> leader);

    List<ClubMember> findByClubId(Long clubId);

    List<ClubMember> findAllClubMemberByMemberId(Long memberId);

    Optional<ClubMember> findByClubIdAndClubMemberRole(Long clubId, ClubMemberRole clubMemberRole);
}
