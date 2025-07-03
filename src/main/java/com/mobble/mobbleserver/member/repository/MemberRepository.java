package com.mobble.mobbleserver.member.repository;

import com.mobble.mobbleserver.account.oauth2.provider.SocialProvider;
import com.mobble.mobbleserver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);

}
