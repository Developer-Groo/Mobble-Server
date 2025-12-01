package com.mobble.mobbleserver.infrastructure.web.account.dto.request;


import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.domain.common.Location;
import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.image.ImageType;
import com.mobble.mobbleserver.domain.member.Gender;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.validation.constraints.*;

public record SignUpRequestDto(
        @NotBlank(message = "name must not be null")
        @Size(max = 10, message = "name must be 10 characters or fewer")
        String name,

        @Min(value = 1, message = "age must be greater than or equal to 1")
        @Max(value = 100, message = "age must be less than or equal to 100")
        int age,

        @NotNull(message = "gender must be not null")
        Gender gender,

        @NotBlank(message = "phone must be not null")
        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
                message = "phone must follow the pattern 010-xxx-xxxx or 010-xxxx-xxxx")
        String phone,

        Location location,

        String profileImageUrl,

        @AssertTrue(message = "terms of service agreement is required")
        boolean termsAgreed,

        @AssertTrue(message = "privacy policy agreement is required")
        boolean privacyAgreed
) {

    public Member toEntity(SocialUserInfo userInfo, Location location) {
        return Member.create(
                this.name,
                this.age,
                this.gender,
                userInfo.email(),
                this.phone,
                location,
                Image.create(profileImageUrl, "", 1L, ImageType.MEMBER_PROFILE),
                this.termsAgreed,
                this.privacyAgreed,
                userInfo.socialProvider(),
                userInfo.socialId()
        );
    }
}
