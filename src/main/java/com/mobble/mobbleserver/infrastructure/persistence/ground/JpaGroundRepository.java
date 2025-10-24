package com.mobble.mobbleserver.infrastructure.persistence.ground;

import com.mobble.mobbleserver.domain.ground.Ground;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGroundRepository extends JpaRepository<Ground, Long> {

}
