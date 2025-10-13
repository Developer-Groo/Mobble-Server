package com.mobble.mobbleserver.refactor.club.core.repository;

import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long>, ClubQueryRepository {
}
