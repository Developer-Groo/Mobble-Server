package com.mobble.mobbleserver.application.chat.room.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ChatRoomBusinessError implements CommonError {
    ;

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
