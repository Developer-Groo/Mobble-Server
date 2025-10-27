package com.mobble.mobbleserver.infrastructure.persistence.club.ageGroup;

import com.mobble.mobbleserver.application.club.ageGroup.port.required.AgeGroupReadPort;
import com.mobble.mobbleserver.application.club.ageGroup.port.required.AgeGroupWritePort;
import com.mobble.mobbleserver.domain.club.ageGroup.AgeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgeGroupPersistenceAdapter implements AgeGroupReadPort, AgeGroupWritePort {

    private final JpaAgeGroupRepository repository;

    @Override
    public List<AgeGroup> findByClubId(Long clubId) {
        return repository.findByClubId(clubId);
    }

    @Override
    public void saveAll(List<AgeGroup> newAgeGroups) {
        repository.saveAll(newAgeGroups);
    }

    @Override
    public void deleteAllClubAgeGroupByClubId(Long clubId) {
        repository.deleteAllClubAgeGroupByClubId(clubId);
    }
}
