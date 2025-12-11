package com.mobble.mobbleserver.domain.chat.message;

import com.mobble.mobbleserver.domain.common.CreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageMention extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_message_mention_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id", nullable = false)
    private ChatMessage chatMessage;

    @Column(name = "mentioned_member_id", nullable = false)
    private Long mentionedMemberId;

    @Builder(access = AccessLevel.PRIVATE)
    private ChatMessageMention(ChatMessage chatMessage, Long mentionedMemberId) {
        this.chatMessage = chatMessage;
        this.mentionedMemberId = mentionedMemberId;
    }

    static ChatMessageMention create(ChatMessage chatMessage, Long mentionedMemberId) {
        return ChatMessageMention.builder()
                .chatMessage(chatMessage)
                .mentionedMemberId(mentionedMemberId)
                .build();
    }
}
