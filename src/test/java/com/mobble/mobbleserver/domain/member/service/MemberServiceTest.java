package com.mobble.mobbleserver.domain.member.service;

import com.mobble.mobbleserver.domain.member.dto.response.MemberResponseDto;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberValidator memberValidator;

    @InjectMocks
    private MemberService memberService;

    private static final Long MEMBER_ID = 1L;

    @Test
    @DisplayName("memberId로 회원 정보 조회 성공")
    void success_when_get_member() {
        // given
        Member member = MemberTestFixture.createDefaultMember();
        given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(member);

        // when
        MemberResponseDto result = memberService.getMember(MEMBER_ID);

        // then
        verify(memberValidator).findMemberByMemberIdOrThrow(MEMBER_ID);
        assertThat(result.memberId()).isEqualTo(member.getId());
        assertThat(result.email()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 예외 발생")
    void fail_when_member_not_found() {
        // given
        given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willThrow(new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));

        // when then
        assertThatThrownBy(() -> memberService.getMember(MEMBER_ID))
                .isInstanceOf(DomainException.class)
                .hasMessage(MemberErrorCode.NOT_FOUND_MEMBER.message());
    }
}
