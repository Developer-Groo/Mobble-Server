package com.mobble.mobbleserver.support.fixture.club;

import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.clubCategory.entity.ClubCategory;

public class ClubTestFixture {

    public static Club createDefaultClub(ClubCategory clubCategory) {
        return Club.createClub(
                clubCategory,
                "name",
                1,
                true
        );
    }
}
