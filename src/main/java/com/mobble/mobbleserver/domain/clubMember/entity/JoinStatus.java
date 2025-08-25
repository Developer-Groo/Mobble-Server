package com.mobble.mobbleserver.domain.clubMember.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JoinStatus {
    WAITING("승인 대기"),
    APPROVED("가입 승인"),
    REJECTED("가입 거절"),
    KICKED("멤버 강퇴"),
    WITHDRAWN("멤버 탈퇴");

    private final String displayName;
}
