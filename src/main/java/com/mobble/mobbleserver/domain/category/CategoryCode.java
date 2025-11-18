package com.mobble.mobbleserver.domain.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryCode {
    CLUB_BADMINTON("배드민턴", CategoryTargetType.CLUB),
    CLUB_SOCCER("축구", CategoryTargetType.CLUB);

    private final String name;
    private final CategoryTargetType targetType;

    public boolean supports(CategoryTargetType targetType) {
        return this.targetType.equals(targetType);
    }
}
