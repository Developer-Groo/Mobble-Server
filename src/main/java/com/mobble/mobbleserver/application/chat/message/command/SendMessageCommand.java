package com.mobble.mobbleserver.application.chat.message.command;

import com.mobble.mobbleserver.domain.chat.message.MessageType;

public record SendMessageCommand(
        Long chatRoomId,
        Long senderId,
        String content,
        MessageType type
) {

    public static SendMessageCommand create(
            Long chatRoomId,
            Long senderId,
            String content,
            MessageType type
    ) {
        return new SendMessageCommand(
                chatRoomId,
                senderId,
                content,
                type
        );
    }
}
