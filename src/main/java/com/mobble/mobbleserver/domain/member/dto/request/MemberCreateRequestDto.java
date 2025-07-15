package com.mobble.mobbleserver.domain.member.dto.request;

import com.mobble.mobbleserver.domain.member.entity.Gender;
import com.mobble.mobbleserver.domain.member.entity.Member;
import jakarta.validation.constraints.*;

public record MemberCreateRequestDto(
        @NotBlank(message = "MEMBER:NAME_NOT_BLANK")
        @Size(max = 10, message = "MEMBER:NAME_TOO_LONG")
        String name,

        @Min(value = 1, message = "MEMBER:AGE_TOO_LOW")
        @Max(value = 100, message = "MEMBER:AGE_TOO_HIGH")
        int age,

        @NotNull(message = "MEMBER:REQUIRED_GENDER")
        Gender gender,

        @NotBlank(message = "MEMBER:REQUIRED_EMAIL")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "MEMBER:WRONG_EMAIL_PATTERN")
        String email,

        String password, // Todo 소셜로그인 구현시 삭제

        @NotBlank(message = "MEMBER:REQUIRED_PHONE")
        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$", message = "MEMBER:WRONG_PHONE_PATTERN")
        String phone,

        @NotBlank(message = "MEMBER:REQUIRED_GROUND")
        String ground,

        String profileImage,

        @AssertTrue(message = "MEMBER:REQUIRED_TERMS_AGREE")
        boolean termsAgreed,

        @AssertTrue(message = "MEMBER:REQUIRED_PRIVACY_AGREE")
        boolean privacyAgreed

//        SocialProvider socialProvider,
//        String socialId
) {
    public Member toEntity() {
        return Member.createMember(
                this.name,
                this.age,
                this.gender,
                this.email,
                this.password, // Todo 소셜로그인 구현시 삭제
                this.phone,
                this.ground,
                this.profileImage,
                this.termsAgreed,
                this.privacyAgreed
//                this.socialProvider,
//                this.socialId
        );
    }
}