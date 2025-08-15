package com.mobble.mobbleserver.domain.club.club.dto.request;

import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.club.clubAgeGroup.entity.ClubAgeGroupType;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ClubRequestDto(
        @NotBlank(message = "CLUB:NAME_NOT_BLANK")
        @Size(max = 20, message = "CLUB:NAME_TOO_LONG")
        String name,

        @NotBlank(message = "CLUB:CATEGORY_NOT_BLANK")
        String category,

//        String profileImage,
//        List<String> infoImage,

        @NotBlank(message = "CLUB:GROUND_NOT_BLANK")
        String ground,

        @NotBlank(message = "CLUB:ADDRESS_NOT_BLANK")
        String address,

        @Min(value = 2, message = "CLUB:HEADCOUNT_MIN")
        @Max(value = 1000, message = "CLUB:HEADCOUNT_MAX")
        int headcount,

        @NotEmpty(message = "CLUB:AGE_GROUP_NOT_EMPTY")
        List<ClubAgeGroupType> ageGroup,

        @NotNull(message = "CLUB:JOIN_TYPE_REQUIRED")
        Boolean isAutoJoin
) {

    public Club toEntity(ClubCategory clubCategory) {
        return Club.createClub(
                clubCategory,
                this.name,
                this.ground,
                this.address,
                this.headcount,
                this.isAutoJoin
        );
    }
}
