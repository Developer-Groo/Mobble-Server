package com.mobble.mobbleserver.infrastructure.persistence.clubCategory;

import com.mobble.mobbleserver.application.clbuCategory.port.ClubCategoryReadPort;
import com.mobble.mobbleserver.domain.ClubCategory.ClubCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClubCategoryPersistenceAdapter implements ClubCategoryReadPort {

    private final  JpaClubCategoryRepository repository;


    @Override
    public Optional<ClubCategory> findByName(String name) {
        return repository.findByName(name);
    }
}
