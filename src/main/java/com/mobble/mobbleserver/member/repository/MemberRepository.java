package com.mobble.mobbleserver.member.repository;

import com.mobble.mobbleserver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

//    Optional<Member> findBySocialTypeAndSocialId(SocialProvider socialType, String socialId);

}
