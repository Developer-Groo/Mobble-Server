package com.mobble.mobbleserver.domain.club.clubAgeGroup.repository;

import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.club.clubAgeGroup.entity.ClubAgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ClubAgeGroupRepository extends JpaRepository<ClubAgeGroup, Long> {

    List<ClubAgeGroup> findByClubId(Long clubId);

    @Modifying
    void deleteAllClubAgeGroupByClubId(Long clubId);
}
