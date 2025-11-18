package com.mobble.mobbleserver.infrastructure.web.club.dto.request;

import com.mobble.mobbleserver.domain.club.AgeGroup;
import com.mobble.mobbleserver.domain.club.Club;
import jakarta.validation.constraints.*;

import java.util.List;

public record ClubRequestDto(
        @NotBlank(message = "CLUB:NAME_NOT_BLANK")
        @Size(max = 20, message = "CLUB:NAME_TOO_LONG")
        String name,

        @NotBlank(message = "CLUB:CATEGORY_NOT_BLANK")
        String category,

//        String profileImage,
//        List<String> infoImage,

        List<Long> groundCodes,

        @Min(value = 2, message = "CLUB:HEADCOUNT_MIN")
        @Max(value = 1000, message = "CLUB:HEADCOUNT_MAX")
        int headcount,

        @NotEmpty(message = "CLUB:AGE_GROUP_NOT_EMPTY")
        List<AgeGroup> ageGroup,

        @NotNull(message = "CLUB:JOIN_TYPE_REQUIRED")
        Boolean isAutoJoin
) {
}
