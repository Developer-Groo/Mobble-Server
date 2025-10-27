package com.mobble.mobbleserver.application.club.ageGroup.port.required;

import com.mobble.mobbleserver.domain.club.ageGroup.AgeGroup;

import java.util.List;

public interface AgeGroupWritePort {

    void saveAll(List<AgeGroup> newAgeGroups);

    void deleteAllClubAgeGroupByClubId(Long clubId);
}
