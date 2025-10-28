package com.mobble.mobbleserver.infrastructure.persistence.clubMember;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface JpaClubMemberRepository extends JpaRepository<ClubMember, Long>, ClubMemberQueryDslRepository {

    Optional<ClubMember> findByClubIdAndClubMemberRole(Long clubId, ClubMemberRole clubMemberRole);

    Optional<ClubMember> findClubMemberByClubIdAndMemberId(Long clubId, Long memberId);

    @Modifying
    void deleteAllClubMemberByClubId(Long clubId);

    long countByClubIdAndJoinStatus(Long clubId, JoinStatus joinStatus);

    List<ClubMember> findByClubId(Long clubId);

    @Query("""
      select distinct cm.clubMemberRole
      from ClubMember cm
      where cm.member.id = :memberId
        and cm.clubMemberRole in :roles
        and cm.member.isDeleted = false
    """)
    List<ClubMemberRole> findDistinctRolesByMemberIdAndRoleIn(Long memberId, Collection<ClubMemberRole> roles);

}
