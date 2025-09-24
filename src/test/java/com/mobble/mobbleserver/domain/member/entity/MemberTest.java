package com.mobble.mobbleserver.domain.member.entity;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    private static final String NAME = "name";
    private static final int AGE = 20;
    private static final Gender GENDER = Gender.MALE;
    private static final String EMAIL = "test@example.com";
    private static final String PHONE = "010-1234-5678";
    private static final String GROUND = "ground";
    private static final String PROFILE_IMAGE = "profile";
    private static final SocialProvider SOCIAL_PROVIDER = SocialProvider.KAKAO;
    private static final String SOCIAL_ID = "1232123";

    @Nested
    @DisplayName("멤버 생성")
    class CreateMember {

        @Test
        @DisplayName("Member 생성 성공")
        void success_create_member() {
            // given & when
            Member member = Member.createMember(
                    NAME, AGE, GENDER, EMAIL, PHONE, GROUND, PROFILE_IMAGE,
                    true, true, SOCIAL_PROVIDER, SOCIAL_ID
            );

            // then
            assertThat(member).isNotNull();
            assertThat(member.getName()).isEqualTo(NAME);
            assertThat(member.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("name 이 null 이면 예외 발생")
        void fail_when_name_null() {
            // when & then
            assertThatThrownBy(() -> Member.createMember(
                    null, AGE, GENDER, EMAIL, PHONE, GROUND, PROFILE_IMAGE,
                    true, true, SOCIAL_PROVIDER, SOCIAL_ID
            ))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(MemberErrorCode.NAME_REQUIRED.message());
        }

        @Test
        @DisplayName("gender 가 null 이면 예외 발생")
        void fail_when_gender_null() {
            // when & then
            assertThatThrownBy(() -> Member.createMember(
                    NAME, AGE, null, EMAIL, PHONE, GROUND, PROFILE_IMAGE,
                    true, true, SOCIAL_PROVIDER, SOCIAL_ID
            ))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(MemberErrorCode.GENDER_REQUIRED.message());
        }
    }
}
