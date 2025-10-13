package com.mobble.mobbleserver.refactor.member.validator;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MemberValidatorTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberValidator memberValidator;

    private static final Long MEMBER_ID = 1L;
    private static final SocialProvider SOCIAL_PROVIDER = SocialProvider.NAVER;
    private static final String SOCIAL_ID = "123456";

    private Member member;

    @BeforeEach
    void setUp() {
        member = mock(Member.class);
    }

    @Nested
    @DisplayName("findMemberByMemberIdOrThrow")
    class FindMemberByMemberIdOrThrow {

        @Test
        @DisplayName("회원 조회 성공")
        void success_when_get_member() {
            // given
            given(memberRepository.findByIdAndIsDeletedFalse(MEMBER_ID)).willReturn(Optional.of(member));

            // when
            Member result = memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID);

            // then
            assertThat(result).isEqualTo(member);
        }

        @Test
        @DisplayName("회원이 존재하지 않으면 예외 발생")
        void fail_when_member_not_found() {
            // given
            given(memberRepository.findByIdAndIsDeletedFalse(MEMBER_ID)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(MemberErrorCode.NOT_FOUND_MEMBER.message());
        }
    }

    @Nested
    @DisplayName("findMemberOrThrowIfDeleted")
    class FindMemberOrThrowIfDeleted {

        @Test
        @DisplayName("삭제되지 않은 회원 조회 성공")
        void success_when_get_member_not_deleted() {
            // given
            given(member.isDeleted()).willReturn(false);
            given(memberRepository.findBySocialProviderAndSocialId(SOCIAL_PROVIDER, SOCIAL_ID)).willReturn(Optional.of(member));

            // when
            Member result = memberValidator.findMemberOrThrowIfDeleted(SOCIAL_PROVIDER, SOCIAL_ID);

            // then
            assertThat(result).isEqualTo(member);
        }

        @Test
        @DisplayName("삭제된 회원 조회시 예외 발생")
        void fail_when_get_member_deleted() {
            // given
            given(member.isDeleted()).willReturn(true);
            given(memberRepository.findBySocialProviderAndSocialId(SOCIAL_PROVIDER, SOCIAL_ID)).willReturn(Optional.of(member));

            // when & then
            assertThatThrownBy(() -> memberValidator.findMemberOrThrowIfDeleted(SOCIAL_PROVIDER, SOCIAL_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(MemberErrorCode.FAILED_JOIN.message());
        }

        @Test
        @DisplayName("존재하지 않는 회원이면 null 반환")
        void success_when_member_not_found() {
            // given
            given(memberRepository.findBySocialProviderAndSocialId(SOCIAL_PROVIDER, SOCIAL_ID)).willReturn(Optional.empty());

            // when
            Member result = memberValidator.findMemberOrThrowIfDeleted(SOCIAL_PROVIDER, SOCIAL_ID);

            // then
            assertThat(result).isNull();
        }
    }
}
