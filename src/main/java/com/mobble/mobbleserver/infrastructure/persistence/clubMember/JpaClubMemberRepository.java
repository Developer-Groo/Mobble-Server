package com.mobble.mobbleserver.infrastructure.persistence.clubMember;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaClubMemberRepository extends JpaRepository<ClubMember, Long>, ClubMemberQueryDslRepository {

    void deleteAllByClubId(Long clubId);

    Optional<ClubMember> findClubMemberByClubIdAndMemberId(Long clubId, Long memberId);

    List<ClubMember> findAllByMemberId(Long memberId);

    List<ClubMember> findByClubId(Long clubId);

    boolean existsByClubIdAndMemberIdAndJoinStatus(Long clubId, Long memberId, JoinStatus joinStatus);
}
