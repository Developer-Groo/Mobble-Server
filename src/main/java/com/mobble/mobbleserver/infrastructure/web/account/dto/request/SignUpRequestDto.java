package com.mobble.mobbleserver.infrastructure.web.account.dto.request;


import com.mobble.mobbleserver.application.account.command.SignUpCommand;
import com.mobble.mobbleserver.domain.member.Gender;
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

        Long profileImageId,

        @NotBlank(message = "address1 must not be blank")
        @Size(max = 100, message = "address1 must be 100 characters or fewer")
        String address1,

        @Size(max = 100, message = "address2 must be 100 characters or fewer")
        String address2,

        @NotBlank(message = "city must not be blank")
        @Size(max = 50, message = "city must be 50 characters or fewer")
        String city,

        @NotBlank(message = "district must not be blank")
        @Size(max = 50, message = "district must be 50 characters or fewer")
        String district,

        @NotNull(message = "latitude must not be null")
        @DecimalMin(value = "-90.0", message = "latitude must be greater than or equal to -90.0")
        @DecimalMax(value = "90.0", message = "latitude must be less than or equal to 90.0")
        Double latitude,

        @NotNull(message = "longitude must not be null")
        @DecimalMin(value = "-180.0", message = "longitude must be greater than or equal to -180.0")
        @DecimalMax(value = "180.0", message = "longitude must be less than or equal to 180.0")
        Double longitude,

        @AssertTrue(message = "terms of service agreement is required")
        boolean termsAgreed,

        @AssertTrue(message = "privacy policy agreement is required")
        boolean privacyAgreed
) {

    public SignUpCommand toCommand(String signUpToken) {
        return SignUpCommand.create(
                name,
                age,
                gender,
                phone,
                profileImageId,
                address1,
                address2,
                city,
                district,
                latitude,
                longitude,
                termsAgreed,
                privacyAgreed,
                signUpToken
        );
    }
}
