package com.mobble.mobbleserver.domain.clubMember.repository;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    Optional<ClubMember> findByClubIdAndClubMemberRole(Long clubId, ClubMemberRole clubMemberRole);

    Optional<ClubMember> findClubMemberByClubIdAndMemberId(Long clubId, Long memberId);

    @Modifying
    void deleteAllClubMemberByClubId(Long clubId);

    long countByClubIdAndJoinStatus(Long clubId, JoinStatus joinStatus);
}
