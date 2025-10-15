package com.mobble.mobbleserver.domain.chat.room;

import com.mobble.mobbleserver.common.baseEntity.CreatedAtEntity;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomParticipant extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "last_read_message_id")
    private Long lastReadMessageId;

    @Column(name = "notified", nullable = false)
    private boolean notified = true;

    @Builder(access = AccessLevel.PRIVATE)
    public ChatRoomParticipant(ChatRoom chatRoom, Member member) {
        this.chatRoom = chatRoom;
        this.member = member;
    }

    static ChatRoomParticipant createChatRoomParticipant(ChatRoom chatRoom, Member member) {
        return ChatRoomParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
    }

    void updateLastReadMessage(Long messageId) {
        if (this.lastReadMessageId == null) throw new IllegalArgumentException("");
        this.lastReadMessageId = messageId;
    }

    LocalDateTime getJoinedAt() {
        return this.createdAt;
    }

    void enableNotified() {
        this.notified = true;
    }

    void disableNotified() {
        this.notified = false;
    }
}
