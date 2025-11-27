package com.mobble.mobbleserver.domain.club;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgeGroup {
    TEEN("10대"),
    TWENTIES("20대"),
    THIRTIES("30대"),
    FORTIES("40대"),
    FIFTIES("50대"),
    SIXTIES_AND_ABOVE("60대 이상"),

    MIXED_2030("20·30대"),
    MIXED_4050("40·50대"),
    ALL("전 연령대");

    private final String displayName;
}
