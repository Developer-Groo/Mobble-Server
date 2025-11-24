package com.mobble.mobbleserver.domain.clubMember;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JoinStatus {
    APPROVED("승인"),
    WAITING("대기"),
    REJECTED("거절"),
    KICKED("강퇴"),
    WITHDRAWN("탈퇴");

    private final String displayName;
}
