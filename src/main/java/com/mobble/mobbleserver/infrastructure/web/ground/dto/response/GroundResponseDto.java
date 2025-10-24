package com.mobble.mobbleserver.refactor.ground.dto.response;

import com.mobble.mobbleserver.refactor.ground.entity.Ground;

public record GroundResponseDto(
        Long code,
        String sido,
        String sigungu,
        String eubmyeondong
) {
    public static GroundResponseDto toDto(Ground ground) {
        return new GroundResponseDto(
                ground.getCode(),
                ground.getSido(),
                ground.getSigungu(),
                ground.getEubmyeondong()
        );
    }
}
