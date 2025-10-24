package com.mobble.mobbleserver.application.ground.required;

import com.mobble.mobbleserver.domain.ground.Ground;

import java.util.List;
import java.util.Optional;

public interface GroundReadPort {

    List<Ground> findAllById(List<Long> codeList);

    Optional<Ground> findById(Long id);
}
