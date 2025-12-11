package com.mobble.mobbleserver.domain.chat.room.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ChatRoomError implements CommonError {
    ALREADY_PARTICIPANT("member already participates in this chat room", ErrorCategory.CONFLICT),
    PARTICIPANT_NOT_FOUND("participant not found in this chat room", ErrorCategory.PERMISSION_DENIED),
    INVALID_ROOM_TYPE("chat room type does not match expected type", ErrorCategory.VALIDATION),
    NOT_DIRECT_ROOM("chat room is not a direct room", ErrorCategory.VALIDATION);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "CHAT_ROOM." + name();
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
