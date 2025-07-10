package com.mobble.mobbleserver.domain.clubAgeGroup.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClubAgeGroupType {
    TEEN("10대"),
    TWENTIES("20대"),
    THIRTIES("30대"),
    FORTIES("40대"),
    FIFTIES("50대"),
    SIXTIES_AND_ABOVE("60대 이상");

    private final String displayName;
}