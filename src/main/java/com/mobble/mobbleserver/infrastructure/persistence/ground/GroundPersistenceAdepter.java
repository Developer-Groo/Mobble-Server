package com.mobble.mobbleserver.infrastructure.persistence.ground;

import com.mobble.mobbleserver.application.ground.required.GroundReadPort;
import com.mobble.mobbleserver.domain.ground.Ground;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroundPersistenceAdepter implements GroundReadPort {

    private final JpaGroundRepository repository;

    @Override
    public List<Ground> findAllById(List<Long> codeList) {
        return repository.findAllById(codeList);
    }

    @Override
    public Optional<Ground> findById(Long id) {
        return repository.findById(id);
    }
}
