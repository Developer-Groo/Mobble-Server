package com.mobble.mobbleserver.domain.clubMember.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClubMemberRole {
    LEADER("모임장"),
    MANAGER("관리자"),
    MEMBER("일반 사용자");

    private final String displayName;
}
