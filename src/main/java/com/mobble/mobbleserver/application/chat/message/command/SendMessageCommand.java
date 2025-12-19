package com.mobble.mobbleserver.application.chat.message.command;

import com.mobble.mobbleserver.domain.chat.message.MessageType;

import java.util.List;

public record SendMessageCommand(
        Long chatRoomId,
        Long senderId,
        String content,
        MessageType type,
        List<Long> mentionedMemberIds
 ) {

    public static SendMessageCommand create(
            Long chatRoomId,
            Long senderId,
            String content,
            MessageType type,
            List<Long> mentionedMemberIds
    ) {
        return new SendMessageCommand(
                chatRoomId,
                senderId,
                content,
                type,
                mentionedMemberIds
        );
    }
}
