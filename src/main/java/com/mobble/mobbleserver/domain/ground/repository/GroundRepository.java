package com.mobble.mobbleserver.domain.ground.repository;

import com.mobble.mobbleserver.domain.ground.entity.Ground;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroundRepository extends JpaRepository<Ground, Long> {

    List<Ground> findAllByCodeIn(List<Long> groundCodes);

    Optional<Ground> findGroundByCode(Long groundCode);
}
