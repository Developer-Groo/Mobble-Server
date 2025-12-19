package com.mobble.mobbleserver.application.chat.message.result;

import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.message.MessageType;
import com.mobble.mobbleserver.util.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageResult(
        Long chatRoomId,
        Long messageId,
        String content,
        MessageType type,
        Long senderId,
        String senderName,
        String senderProfileImageUrl,
        LocalDateTime sentAt,
        List<Long> mentionedMemberIds
) {

    public static ChatMessageResult create(ChatMessage chatMessage) {
        return new ChatMessageResult(
                chatMessage.getChatRoom().getId(),
                chatMessage.getId(),
                chatMessage.getContent(),
                chatMessage.getType(),
                chatMessage.getSender().getId(),
                chatMessage.getSender().getName(),
                chatMessage.getSender().getProfileImage() != null
                        ? chatMessage.getSender().getProfileImage().getUrl()
                        : null,
                DateTimeUtils.toKST(chatMessage.getCreatedAt()),
                chatMessage.getMentionedMemberIds()
        );
    }
}
