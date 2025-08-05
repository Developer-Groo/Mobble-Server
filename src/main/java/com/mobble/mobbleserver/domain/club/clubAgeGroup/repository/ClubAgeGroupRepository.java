package com.mobble.mobbleserver.domain.club.clubAgeGroup.repository;

import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.club.clubAgeGroup.entity.ClubAgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubAgeGroupRepository extends JpaRepository<ClubAgeGroup, Long>, ClubAgeGroupQueryDslRepository {

    List<ClubAgeGroup> findByClub(Club club);
}
