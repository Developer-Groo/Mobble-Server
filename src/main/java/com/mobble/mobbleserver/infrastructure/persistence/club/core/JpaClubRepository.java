package com.mobble.mobbleserver.infrastructure.persistence.club.core;

import com.mobble.mobbleserver.domain.club.core.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaClubRepository extends JpaRepository<Club, Long>, ClubQueryDslRepository {

}
