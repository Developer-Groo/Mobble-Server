package com.mobble.mobbleserver.domain.member.service;

import com.mobble.mobbleserver.domain.member.dto.response.MemberResponseDto;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
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
}
