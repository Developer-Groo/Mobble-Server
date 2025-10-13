package com.mobble.mobbleserver.refactor.clubCategory.repository;

import com.mobble.mobbleserver.refactor.clubCategory.entity.ClubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubCategoryRepository extends JpaRepository<ClubCategory, Long> {
    Optional<ClubCategory> findByName(String name);
}
