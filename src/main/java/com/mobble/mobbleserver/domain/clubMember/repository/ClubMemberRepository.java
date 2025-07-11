package com.mobble.mobbleserver.domain.clubMember.repository;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    Optional<ClubMember> findByClubIdAndMemberId(Long clubId, Long memberId);
}
