package com.mobble.mobbleserver.domain.chat.chatRoomParticipant.entity;

import com.mobble.mobbleserver.common.baseEntity.CreatedAtEntity;
import com.mobble.mobbleserver.domain.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.domain.chat.chatRoom.entity.ChatRoom;
import com.mobble.mobbleserver.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_read_message_id")
    private ChatMessage lastReadMessage;

    @Column(name = "notified", nullable = false)
    private boolean notified = true;

    @Builder(access = AccessLevel.PRIVATE)
    public ChatRoomParticipant(ChatRoom chatRoom, Member member) {
        this.chatRoom = chatRoom;
        this.member = member;
    }

    public static ChatRoomParticipant createChatRoomParticipant(ChatRoom chatRoom, Member member) {
        return ChatRoomParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
    }

    public void updateLastReadMessage(ChatMessage chatMessage) {
        if (lastReadMessage == null) throw new IllegalArgumentException(""); // Todo: ErrorCode 적용
        this.lastReadMessage = chatMessage;
    }

    public void enableNotified() {
        this.notified = true;
    }

    public void disableNotified() {
        this.notified = false;
    }
}
