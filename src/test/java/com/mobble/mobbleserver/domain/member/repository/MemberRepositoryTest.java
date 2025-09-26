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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
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
    private static final String SOCIAL_ID = "1212";

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

    @Nested
    @DisplayName("findAllByIsDeletedTrueAndDeletedAtBefore")
    class FindAllByIsDeletedTrueAndDeletedAtBefore {

        @Test
        @DisplayName("deletedAt이 기준일 이전이면 조회 성공")
        void success_when_deleted_before_standard_day() {
            // given
            LocalDateTime standardDay = LocalDateTime.now();
            Member deletedMember = MemberTestFixture.createDefaultMember();
            deletedMember.softDelete();
            ReflectionTestUtils.setField(deletedMember, "deletedAt", standardDay.minusHours(1));
            memberRepository.save(deletedMember);
            em.flush();
            em.clear();

            // when
            List<Member> result = memberRepository.findAllByIsDeletedTrueAndDeletedAtBefore(standardDay);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getDeletedAt()).isBefore(standardDay);
        }

        @Test
        @DisplayName("deletedAt이 기준일 이후면 조회 실패")
        void fail_when_deleted_after_standard_day() {
            // given
            LocalDateTime standardDay = LocalDateTime.now();
            Member deletedMember = MemberTestFixture.createDefaultMember();
            deletedMember.softDelete();
            ReflectionTestUtils.setField(deletedMember, "deletedAt", standardDay.plusHours(1));
            memberRepository.save(deletedMember);
            em.flush();
            em.clear();

            // when
            List<Member> result = memberRepository.findAllByIsDeletedTrueAndDeletedAtBefore(standardDay);

            // then
            assertThat(result).isEmpty();
        }
    }
}
