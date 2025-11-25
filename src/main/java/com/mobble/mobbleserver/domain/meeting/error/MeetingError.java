package com.mobble.mobbleserver.domain.meeting.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MeetingError implements CommonError {
    INVALID_DATETIME("datetime must not be in the past", ErrorCategory.VALIDATION),
    INVALID_MEMBER_LIMIT("memberLimit must be at least 1", ErrorCategory.VALIDATION),
    FULL_CAPACITY("meeting has reached full capacity", ErrorCategory.CONFLICT);


    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "Meeting." + name();
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
