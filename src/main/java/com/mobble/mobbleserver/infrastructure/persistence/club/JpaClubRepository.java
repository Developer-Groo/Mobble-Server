package com.mobble.mobbleserver.infrastructure.persistence.club;

import com.mobble.mobbleserver.domain.club.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaClubRepository extends JpaRepository<Club, Long> {
}
