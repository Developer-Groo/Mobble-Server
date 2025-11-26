package com.mobble.mobbleserver.application.clubMember.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClubMemberBusinessError implements CommonError {
    NOT_JOINED_CLUB("member is not a participant of this club", ErrorCategory.NOT_FOUND),
    ONLY_LEADER_ALLOWED("only the club leader is allowed to perform this action", ErrorCategory.PERMISSION_DENIED),
    CANNOT_MODIFY_LEADER("leader cannot modify their own status or role", ErrorCategory.PERMISSION_DENIED),
    INVALID_ROLE_CHANGE("invalid role change", ErrorCategory.CONFLICT),
    INVALID_JOIN_STATUS_TRANSITION("invalid join status transition", ErrorCategory.CONFLICT),
    LEADER_CANNOT_LEAVE("club leader cannot leave the club", ErrorCategory.PERMISSION_DENIED),
    ALREADY_ACTIVE_MEMBER("member is already active in this club", ErrorCategory.CONFLICT),
    CANNOT_REJOIN_YET("member cannot rejoin the club yet", ErrorCategory.CONFLICT),
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
