package com.mobble.mobbleserver.support.fixture.club;

import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;

public class ClubTestFixture {

    public static Club createDefaultClub(ClubCategory clubCategory) {
        return Club.createClub(
                clubCategory,
                "name",
                "ground",
                "address",
                1,
                true
        );
    }
}
