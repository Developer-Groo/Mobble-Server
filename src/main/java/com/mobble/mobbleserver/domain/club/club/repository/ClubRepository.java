package com.mobble.mobbleserver.domain.club.club.repository;

import com.mobble.mobbleserver.domain.club.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long>, ClubQueryRepository {
}
