package com.mobble.mobbleserver.infrastructure.web.member.dto.request;

import com.mobble.mobbleserver.application.member.command.UpdateMemberCommand;
import jakarta.validation.constraints.*;

public record UpdateMemberRequestDto(
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
        Double longitude
        ) {

    public UpdateMemberCommand toCommand(Long memberId) {
        return UpdateMemberCommand.create(
                memberId,
                profileImageId,
                address1,
                address2,
                city,
                district,
                latitude,
                longitude
        );
    }
}
