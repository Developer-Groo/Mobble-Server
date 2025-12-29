package com.mobble.mobbleserver.application.chat.room.result;

import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.member.Member;

public record DirectChatRoomPreviewResult(
        Long chatRoomId,
        Long receiverId,
        String receiverName,
        String receiverProfileImageUrl
) {

    public static DirectChatRoomPreviewResult create(ChatRoom chatRoom, Member receiver) {
        return new DirectChatRoomPreviewResult(
                chatRoom.getId(),
                receiver.getId(),
                receiver.getName(),
                receiver.getProfileImage() != null ? receiver.getProfileImage().getUrl() : null
        );
    }
}
