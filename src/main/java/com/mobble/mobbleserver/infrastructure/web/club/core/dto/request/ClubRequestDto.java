package com.mobble.mobbleserver.refactor.club.core.dto.request;

import com.mobble.mobbleserver.refactor.adress.dto.request.AddressRequestDto;
import com.mobble.mobbleserver.refactor.club.ageGroup.entity.AgeGroupType;
import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.clubCategory.entity.ClubCategory;
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

        AddressRequestDto addressDto,

        List<Long> groundCodes,

        @Min(value = 2, message = "CLUB:HEADCOUNT_MIN")
        @Max(value = 1000, message = "CLUB:HEADCOUNT_MAX")
        int headcount,

        @NotEmpty(message = "CLUB:AGE_GROUP_NOT_EMPTY")
        List<AgeGroupType> ageGroup,

        @NotNull(message = "CLUB:JOIN_TYPE_REQUIRED")
        Boolean isAutoJoin
) {

    public Club toEntity(ClubCategory clubCategory) {
        return Club.createClub(
                clubCategory,
                this.name,
                this.headcount,
                this.isAutoJoin
        );
    }
}
