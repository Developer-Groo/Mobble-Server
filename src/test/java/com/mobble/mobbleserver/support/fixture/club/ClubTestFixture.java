package com.mobble.mobbleserver.support.fixture.club;

import com.mobble.mobbleserver.domain.club.entity.Club;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;

public class ClubTestFixture {

    public static Club createDefaultClub() {
        ClubCategory category = ClubCategory.createClubCategory("SOCCER");

        return Club.createClub(
                category,
                "name",
                "ground",
                "address",
                1,
                true
        );
    }
}
