package com.mobble.mobbleserver.domain.clubMember.repository;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
}
