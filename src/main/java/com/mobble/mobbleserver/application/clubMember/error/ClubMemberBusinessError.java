package com.mobble.mobbleserver.application.clubMember.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClubMemberBusinessError implements CommonError {
    NOT_JOINED_CLUB("member is not a participant of this club", ErrorCategory.NOT_FOUND),
    ONLY_LEADER_ALLOWED("only the club leader is allowed to perform this action", ErrorCategory.PERMISSION_DENIED),
    SELF_ROLE_CHANGE_NOT_ALLOWED("cannot change your own role", ErrorCategory.PERMISSION_DENIED),
    NO_PERMISSION("you do not have permission to perform this action", ErrorCategory.PERMISSION_DENIED);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "CLUB_MEMBER_BUSINESS." + name();
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
