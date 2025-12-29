package com.mobble.mobbleserver.application.chat.room.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ChatRoomBusinessError implements CommonError {
    NOT_FOUND("chat room not found", ErrorCategory.NOT_FOUND),
    ALREADY_EXISTS("chat room already exists", ErrorCategory.CONFLICT),
    NOT_PARTICIPANT("member is not a participant of this chat room", ErrorCategory.PERMISSION_DENIED),
    INVALID_FILTER("invalid chat room filter", ErrorCategory.VALIDATION);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "CHAT_ROOM_BUSINESS." + name();
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
