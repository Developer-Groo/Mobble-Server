package com.mobble.mobbleserver.infrastructure.persistence.club.ageGroup;

import com.mobble.mobbleserver.domain.club.ageGroup.AgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface JpaAgeGroupRepository extends JpaRepository<AgeGroup, Long> {

    List<AgeGroup> findByClubId(Long clubId);

    @Modifying
    void deleteAllClubAgeGroupByClubId(Long clubId);
}

