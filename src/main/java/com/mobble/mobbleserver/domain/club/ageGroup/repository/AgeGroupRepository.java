package com.mobble.mobbleserver.domain.club.ageGroup.repository;

import com.mobble.mobbleserver.domain.club.ageGroup.entity.AgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface AgeGroupRepository extends JpaRepository<AgeGroup, Long> {

    List<AgeGroup> findByClubId(Long clubId);

    @Modifying
    void deleteAllClubAgeGroupByClubId(Long clubId);
}
