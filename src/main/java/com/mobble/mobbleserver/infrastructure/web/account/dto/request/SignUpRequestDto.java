package com.mobble.mobbleserver.infrastructure.web.account.dto.request;

import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.domain.ground.Ground;
import com.mobble.mobbleserver.domain.member.Gender;
import com.mobble.mobbleserver.domain.member.Member;
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

        Long groundCode,

        String profileImage,

        @AssertTrue(message = "MEMBER:REQUIRED_TERMS_AGREE")
        boolean termsAgreed,

        @AssertTrue(message = "MEMBER:REQUIRED_PRIVACY_AGREE")
        boolean privacyAgreed
) {

    public Member toEntity(SocialUserInfo userInfo, Ground ground) {
        return Member.createMember(
                this.name,
                this.age,
                this.gender,
                userInfo.email(),
                this.phone,
                ground,
                this.profileImage,
                this.termsAgreed,
                this.privacyAgreed,
                userInfo.socialProvider(),
                userInfo.socialId()
        );
    }
}
