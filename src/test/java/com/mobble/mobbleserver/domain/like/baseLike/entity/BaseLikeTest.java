package com.mobble.mobbleserver.domain.like.baseLike.entity;

import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class BaseLikeTest {

    // 테스트용 구현체
    static class TestLike extends BaseLike {
        public static TestLike create(Member member) {
            TestLike like = new TestLike();
            like.assignMember(member);
            return like;
        }
    }

    private final Member mockMember = MemberTestFixture.createDefaultMember();

    @Nested
    @DisplayName("Member 주입 테스트")
    class AssignMemberTest {

        @Test
        @DisplayName("Member 주입 성공")
        void assignMember_success() {
            // when
            TestLike testLike = TestLike.create(mockMember);

            // then
            assertThat(testLike.getMember()).isEqualTo(mockMember);
        }

        @Test
        @DisplayName("Member가 nulldls 인 경우 예외 발생")
        void fails_when_assignMember_member_is_null() {
            //when & then
            assertThatThrownBy(() -> TestLike.create(null))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(LikeErrorCode.MEMBER_REQUIRED.message());
        }
    }
}
