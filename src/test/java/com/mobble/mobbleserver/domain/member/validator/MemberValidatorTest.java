package com.mobble.mobbleserver.domain.member.validator;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

    @Test
    @DisplayName("회원 조회 성공")
    void success_when_get_member() {
        // given
        Member member = mock(Member.class);
        given(memberRepository.findByIdAndIsDeletedFalse(MEMBER_ID)).willReturn(Optional.of(member));

        // when
        Member result = memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID);

        // then
        Assertions.assertThat(result).isEqualTo(member);
    }

}
