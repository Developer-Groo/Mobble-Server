package com.mobble.mobbleserver.support.fixture.club;

import com.mobble.mobbleserver.domain.ClubCategory.ClubCategory;
import com.mobble.mobbleserver.domain.club.Club;

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
