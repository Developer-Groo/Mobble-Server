package com.mobble.mobbleserver.refactor.member.repository;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.refactor.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 활성 회원(isDeleted = false)만 조회하는 메소드
    Optional<Member> findByIdAndIsDeletedFalse(Long memberId);

    // soft-delete(isDeleted = true) 회원을 포함한 모든 회원 조회
    List<Member> findAllByIsDeletedTrueAndDeletedAtBefore(LocalDateTime withdrewDate);

    Optional<Member> findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
}
