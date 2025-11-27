package com.mobble.mobbleserver.domain.clubMember;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClubMemberRole {
    LEADER("리더"),
    MANAGER("매니저"),
    MEMBER("멤버");

    private final String displayName;

    public boolean canChangeTo(ClubMemberRole target) {
        if (this == target) return false;

        if (this == LEADER) return false;
        if (target == LEADER) return false;

        if (this == MANAGER || this == MEMBER) {
            return target == MANAGER || target == MEMBER;
        }

        return false;
    }
}
