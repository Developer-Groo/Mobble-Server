package com.mobble.mobbleserver.domain.member.repository;

import com.mobble.mobbleserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    List<Member> findWithdrewMembers(LocalDateTime withdrewDate);
}
