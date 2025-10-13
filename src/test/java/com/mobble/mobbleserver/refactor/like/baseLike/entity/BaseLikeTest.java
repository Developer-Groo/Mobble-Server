package com.mobble.mobbleserver.refactor.like.baseLike.entity;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class BaseLikeTest {

    private final Member mockMember = MemberTestFixture.createDefaultMember();
    private final LikeTestFixture mockTestLike = LikeTestFixture.create(mockMember);


    @Test
    @DisplayName("Member 주입 성공")
    void assignMember_success() {
        // then
        assertThat(mockTestLike.getMember()).isEqualTo(mockMember);
    }

    @Test
    @DisplayName("Member 가 null 인 경우 예외 발생")
    void fails_when_assignMember_member_is_null() {
        //when & then
        assertThatThrownBy(() -> LikeTestFixture.create(null))
                .isInstanceOf(DomainException.class)
                .hasMessage(LikeErrorCode.MEMBER_REQUIRED.message());
    }
}
