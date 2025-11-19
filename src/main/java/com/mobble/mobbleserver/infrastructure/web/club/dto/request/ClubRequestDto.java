package com.mobble.mobbleserver.infrastructure.web.club.dto.request;

import com.mobble.mobbleserver.domain.category.CategoryCode;
import com.mobble.mobbleserver.domain.club.AgeGroup;
import jakarta.validation.constraints.*;

public record ClubRequestDto(
        @NotBlank(message = "club name must not be blank")
        @Size(max = 20, message = "club name must be 20 characters or fewer")
        String name,

        @Size(max = 300, message = "description must be 300 characters or fewer")
        String description,

        @NotNull(message = "auto join flag must not be null")
        Boolean isAutoJoin,

        @NotNull(message = "category must not be null")
        CategoryCode category,

        @NotNull(message = "age group must not be null")
        AgeGroup ageGroup,

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

        @Positive(message = "main image id must be positive")
        Long mainImageId
) {
}
