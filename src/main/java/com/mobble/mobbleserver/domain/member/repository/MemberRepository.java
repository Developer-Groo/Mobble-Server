package com.mobble.mobbleserver.domain.member.repository;

import com.mobble.mobbleserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 활성 회원(isDeleted = false)만 조회하는 메소드
    Optional<Member> findByIdAndIsDeletedFalse(Long memberId);

    Optional<Member> findByEmailAndIsDeletedFalse(String email);

    boolean existsByEmailAndIsDeletedFalse(String email);

    // soft-delete(isDeleted = true) 회원을 포함한 모든 회원 조회
    List<Member> findAllByIsDeletedTrueAndDeletedAtBefore(LocalDateTime withdrewDate);

    boolean existsByEmailAndIsDeletedTrue(String email);
}
