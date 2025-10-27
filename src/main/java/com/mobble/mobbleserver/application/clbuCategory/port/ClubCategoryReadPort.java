package com.mobble.mobbleserver.application.clbuCategory.port;

import com.mobble.mobbleserver.domain.ClubCategory.ClubCategory;

import java.util.Optional;

public interface ClubCategoryReadPort {

    Optional<ClubCategory> findByName(String name);

}
