package com.mobble.mobbleserver.refactor.member.entity;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.ground.entity.Ground;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    private static final String NAME = "name";
    private static final int AGE = 20;
    private static final Gender GENDER = Gender.MALE;
    private static final String EMAIL = "test@example.com";
    private static final String PHONE = "010-1234-5678";
    private static final Ground GROUND = null;
    private static final String PROFILE_IMAGE = "profile";
    private static final SocialProvider SOCIAL_PROVIDER = SocialProvider.KAKAO;
    private static final String SOCIAL_ID = "1232123";

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.createMember(
                NAME, AGE, GENDER, EMAIL, PHONE, GROUND, PROFILE_IMAGE,
                true, true, SOCIAL_PROVIDER, SOCIAL_ID
        );
    }

    @Nested
    @DisplayName("멤버 생성")
    class CreateMember {

        @Test
        @DisplayName("Member 생성 성공")
        void success_create_member() {
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

        @Test
        @DisplayName("phone 이 null 이면 예외 발생")
        void fail_when_phone_null() {
            // when & then
            assertThatThrownBy(() -> Member.createMember(
                    NAME, AGE, GENDER, EMAIL, null, GROUND, PROFILE_IMAGE,
                    true, true, SOCIAL_PROVIDER, SOCIAL_ID
            ))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(MemberErrorCode.PHONE_REQUIRED.message());
        }

//        @Test
//        @DisplayName("ground 가 null 이면 예외 발생")
//        void fail_when_ground_null() {
//            // when & then
//            assertThatThrownBy(() -> Member.createMember(
//                    NAME, AGE, GENDER, EMAIL, PHONE, null, PROFILE_IMAGE,
//                    true, true, SOCIAL_PROVIDER, SOCIAL_ID
//            ))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MemberErrorCode.GROUND_REQUIRED.message());
//        }

        @Test
        @DisplayName("termsAgreed 가 false 면 예외 발생")
        void fail_when_terms_not_agreed() {
            // when & then
            assertThatThrownBy(() -> Member.createMember(
                    NAME, AGE, GENDER, EMAIL, PHONE, GROUND, PROFILE_IMAGE,
                    false, true, SOCIAL_PROVIDER, SOCIAL_ID
            ))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(MemberErrorCode.TERMS_AGREED_REQUIRED.message());
        }

        @Test
        @DisplayName("privacyAgreed 가 false 면 예외 발생")
        void fail_when_privacy_not_agreed() {
            // when & then
            assertThatThrownBy(() -> Member.createMember(
                    NAME, AGE, GENDER, EMAIL, PHONE, GROUND, PROFILE_IMAGE,
                    true, false, SOCIAL_PROVIDER, SOCIAL_ID
            ))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(MemberErrorCode.PRIVACY_AGREED_REQUIRED.message());
        }
    }

    @Nested
    @DisplayName("멤버 수정")
    class UpdateMember {

//        @Test
//        @DisplayName("ground, profileImage 수정 성공")
//        void success_when_update_ground_and_profile_image() {
//            // when
//            member.updateMember(null, "new profileImage");
//
//            // then
//            assertThat(member.getGround()).isEqualTo("new Ground");
//            assertThat(member.getProfileImage()).isEqualTo("new profileImage");
//        }

//        @Test
//        @DisplayName("ground 가 null 이면 예외 발생")
//        void fail_when_ground_null() {
//            // when & then
//            assertThatThrownBy(() -> member.updateMember(null, "new profileImage"))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MemberErrorCode.GROUND_REQUIRED.message());
//        }
    }

    @Nested
    @DisplayName("softDelete")
    class SoftDelete {

        @Test
        @DisplayName("Soft Delete 성공")
        void success_soft_delete() {
            // when
            member.softDelete();

            // then
            assertThat(member.isDeleted()).isTrue();
            assertThat(member.getDeletedAt())
                    .isNotNull()
                    .isBeforeOrEqualTo(LocalDateTime.now());
        }
    }
}
