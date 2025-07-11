package com.mobble.mobbleserver.support.fixture.club;

import com.mobble.mobbleserver.domain.club.entity.Club;

public class ClubTestFixture {

    public static Club createDefaultClub() {
        return Club.createClub(
                "name",
                "ground",
                1,
                true
        );
    }
}
