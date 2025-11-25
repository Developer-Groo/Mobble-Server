package com.mobble.mobbleserver.domain.clubMember;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum JoinStatus {
    APPROVED("승인", false),
    WAITING("대기", false),
    REJECTED("거절", true),
    KICKED("강퇴", true),
    WITHDRAWN("탈퇴", true);

    private final String displayName;
    private final boolean rejoinable;

    public boolean canTransitionTo(JoinStatus target) {
        if (this == target) return true;

        return switch (this) {
            case WAITING -> allowedTargets(JoinStatus.APPROVED, JoinStatus.REJECTED).contains(target);
            case APPROVED -> allowedTargets(JoinStatus.KICKED).contains(target);
            case REJECTED, KICKED, WITHDRAWN -> false;
        };
    }

    private Set<JoinStatus> allowedTargets(JoinStatus... statuses) {
        return EnumSet.of(statuses[0], statuses);
    }
}
