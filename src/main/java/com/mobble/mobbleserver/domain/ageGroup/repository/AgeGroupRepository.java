package com.mobble.mobbleserver.domain.ageGroup.repository;

import com.mobble.mobbleserver.domain.ageGroup.entity.AgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgeGroupRepository extends JpaRepository<AgeGroup,Long> {
}
