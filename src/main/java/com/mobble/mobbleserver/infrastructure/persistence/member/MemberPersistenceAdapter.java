package com.mobble.mobbleserver.infrastructure.persistence.member;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberWritePort;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberWritePort, MemberReadPort {

    private final JpaMemberRepository repository;

    /* MemberWritePort */
    @Override
    public Member save(Member member) {
        return repository.save(member);
    }

    @Override
    public void deleteAll(Member member) {
        repository.deleteAll();
    }

    /* MemberReadPort */
    @Override
    public Optional<Member> findByIdAndIsDeletedFalse(Long memberId) {
        return repository.findByIdAndIsDeletedFalse(memberId);
    }

    @Override
    public List<Member> findAllByIsDeletedTrueAndDeletedAtBefore(LocalDateTime withdrewDate) {
        return repository.findAllByIsDeletedTrueAndDeletedAtBefore(withdrewDate);
    }

    @Override
    public Optional<Member> findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId) {
        return repository.findBySocialProviderAndSocialId(socialProvider, socialId);
    }
}
