package com.mobble.mobbleserver.domain.ground.dto.response;

import com.mobble.mobbleserver.domain.ground.entity.Ground;

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
