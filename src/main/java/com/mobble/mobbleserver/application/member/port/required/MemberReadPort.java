package com.mobble.mobbleserver.application.member.port.required;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.domain.member.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberReadPort {

    // 활성 회원(isDeleted = false)만 조회하는 메소드
    Optional<Member> findByIdAndIsDeletedFalse(Long memberId);

    // soft-delete(isDeleted = true) 회원을 포함한 모든 회원 조회
    List<Member> findAllByIsDeletedTrueAndDeletedAtBefore(LocalDateTime softDeletedDate);

    Optional<Member> findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
}
