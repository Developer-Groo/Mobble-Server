package com.mobble.mobbleserver.domain.clubMember.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClubMemberError implements CommonError {
    MEMBER_NOT_APPROVED("member is not approved", ErrorCategory.PERMISSION_DENIED);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "CLUB_MEMBER." + name();
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public ErrorCategory category() {
        return category;
    }
}
