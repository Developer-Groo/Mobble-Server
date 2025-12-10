package com.mobble.mobbleserver.application.chat.room.result;

import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.ChatRoomType;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.member.Member;

import java.time.LocalDateTime;
import java.util.Map;

public record ChatRoomPreviewResult(
        Long chatRoomId,
        ChatRoomType roomType,

        Long clubId,
        Long otherMemberId,

        String title,
        String imageUrl,

        String latestMessage,
        LocalDateTime latestMessageAt,

        int unreadCount,
        Long lastReadMessageId
) {

    public static ChatRoomPreviewResult create(
            Member member,
            ChatRoom chatRoom,
            Map<Long, Long> lastReadMessageIdsByChatRoom,
            Map<Long, ChatMessage> latestMessagesMap,
            Map<Long, Integer> unreadCountMap
    ) {
        Long chatRoomId = chatRoom.getId();

        Long lastReadMessageId = lastReadMessageIdsByChatRoom.getOrDefault(chatRoomId, 0L);
        ChatMessage lastMessage = latestMessagesMap.get(chatRoomId);
        Integer unreadCount = unreadCountMap.getOrDefault(chatRoomId, 0);

        ChatRoomType type = chatRoom.getType();

        Long clubId;
        Long otherMemberId;
        String title;
        String imageUrl;

        if (type == ChatRoomType.DIRECT) {
            Member other = chatRoom.getReceiverFor(member.getId());

            clubId = null;
            otherMemberId = other.getId();
            title = other.getName();
            imageUrl = (other.getProfileImage() != null)
                    ? other.getProfileImage().getUrl()
                    : null;
        } else if (type == ChatRoomType.GROUP) {
            Club club = chatRoom.getClub();

            clubId = club.getId();
            otherMemberId = null;
            title = club.getName();
            imageUrl = (club.getMainImage() != null)
                    ? club.getMainImage().getUrl()
                    : null;
        } else {
            throw new IllegalStateException("Unsupported chat room type: " + type); // Todo: Error 수정
        }

        return new ChatRoomPreviewResult(
                chatRoomId,
                type,

                clubId,
                otherMemberId,

                title,
                imageUrl,

                lastMessage != null ? lastMessage.getContent() : null,
                lastMessage != null ? lastMessage.getCreatedAt() : null,

                unreadCount,
                lastReadMessageId
        );
    }
}
