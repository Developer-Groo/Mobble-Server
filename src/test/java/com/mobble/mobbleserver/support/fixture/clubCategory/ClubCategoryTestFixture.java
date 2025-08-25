package com.mobble.mobbleserver.support.fixture.clubCategory;

import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;

public class ClubCategoryTestFixture {

    public static ClubCategory createDefaultCategory() {
        return ClubCategory.createClubCategory("SOCCER");
    }
}
