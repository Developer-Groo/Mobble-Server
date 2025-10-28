package com.mobble.mobbleserver.application.club.ageGroup.port.required;

import com.mobble.mobbleserver.domain.club.ageGroup.AgeGroup;

import java.util.List;

public interface AgeGroupReadPort {

    List<AgeGroup> findByClubId(Long clubId);
}
