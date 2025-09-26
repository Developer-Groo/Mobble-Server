package com.mobble.mobbleserver.domain.member.repository;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    private static final SocialProvider SOCIAL_PROVIDER = SocialProvider.NAVER;
    private static final String SOCIAL_ID = "123456";

    @Nested
    @DisplayName("findByIdAndIsDeletedFalse")
    class FindByIdAndIsDeletedFalse {

        @Test
        @DisplayName("활성 회원 조회 성공")
        void success_when_get_member_not_deleted() {
            // given
            Member member = MemberTestFixture.createDefaultMember();
            memberRepository.save(member);
            em.flush();
            em.clear();

            // when
            Optional<Member> result = memberRepository.findByIdAndIsDeletedFalse(member.getId());

            // then
            assertThat(result).isPresent();
            assertThat(result.get().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제 회원 조회 실패")
        void fail_when_member_is_deleted() {
            // given
            Member deletedMember = MemberTestFixture.createDefaultMember();
            deletedMember.softDelete();
            memberRepository.save(deletedMember);
            em.flush();
            em.clear();

            // when
            Optional<Member> result = memberRepository.findByIdAndIsDeletedFalse(deletedMember.getId());

            // then
            assertThat(result).isNotPresent();
        }
    }
}
