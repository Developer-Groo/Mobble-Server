package com.mobble.mobbleserver.domain.chat.chatRoom.entity;

import com.mobble.mobbleserver.common.baseEntity.CreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ChatRoomType type;

    @Builder(access = AccessLevel.PRIVATE)
    private ChatRoom(ChatRoomType type) {
        this.type = type;
    }

    public static ChatRoom createChatRoom(ChatRoomType type) {
        return ChatRoom.builder()
                .type(type)
                .build();
    }
}
