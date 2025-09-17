package com.mobble.mobbleserver.account.auth.dto.request;

import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;
import com.mobble.mobbleserver.domain.member.entity.Gender;
import com.mobble.mobbleserver.domain.member.entity.Member;
import jakarta.validation.constraints.*;

public record SignUpRequestDto(
        @NotBlank(message = "MEMBER:NAME_NOT_BLANK")
        String name,

        @Min(value = 1, message = "MEMBER:AGE_TOO_LOW")
        @Max(value = 100, message = "MEMBER:AGE_TOO_HIGH")
        int age,

        @NotNull(message = "MEMBER:REQUIRED_GENDER")
        Gender gender,

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
) {

    public Member toEntity(SocialUserInfo userInfo) {
        return Member.createMember(
                this.name,
                this.age,
                this.gender,
                userInfo.email(),
                this.phone,
                this.ground,
                this.profileImage,
                this.termsAgreed,
                this.privacyAgreed,
                userInfo.socialProvider(),
                userInfo.socialId()
        );
    }
}
