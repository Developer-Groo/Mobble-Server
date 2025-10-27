package com.mobble.mobbleserver.infrastructure.persistence.clubCategory;

import com.mobble.mobbleserver.domain.ClubCategory.ClubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaClubCategoryRepository extends JpaRepository<ClubCategory, Long> {

    Optional<ClubCategory> findByName(String name);
}
