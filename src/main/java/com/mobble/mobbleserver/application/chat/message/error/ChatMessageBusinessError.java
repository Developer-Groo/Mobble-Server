package com.mobble.mobbleserver.application.chat.message.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ChatMessageBusinessError implements CommonError {
    INVALID_CURSOR("cursor must include both id and createdAt", ErrorCategory.VALIDATION),
    NOT_FOUND("message not found", ErrorCategory.NOT_FOUND),
    NOT_BELONG_TO_CHAT_ROOM("message does not belong to this chat room", ErrorCategory.VALIDATION);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "CHAT_MESSAGE_BUSINESS." + name();
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
