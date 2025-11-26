package com.mobble.mobbleserver.application.meeting.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MeetingBusinessError implements CommonError {
    NOT_FOUND("meeting not found", ErrorCategory.NOT_FOUND);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "MEETING_BUSINESS" + name();
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
