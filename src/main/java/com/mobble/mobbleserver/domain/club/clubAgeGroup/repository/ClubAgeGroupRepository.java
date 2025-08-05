package com.mobble.mobbleserver.domain.clubAgeGroup.repository;

import com.mobble.mobbleserver.domain.club.entity.Club;
import com.mobble.mobbleserver.domain.clubAgeGroup.entity.ClubAgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubAgeGroupRepository extends JpaRepository<ClubAgeGroup, Long>, ClubAgeGroupQueryDslRepository {

    List<ClubAgeGroup> findByClub(Club club);
}
