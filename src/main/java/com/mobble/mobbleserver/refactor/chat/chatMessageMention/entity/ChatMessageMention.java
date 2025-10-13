package com.mobble.mobbleserver.refactor.chat.chatMessageMention.entity;

import com.mobble.mobbleserver.common.baseEntity.CreatedAtEntity;
import com.mobble.mobbleserver.refactor.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.refactor.member.entity.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentioned_member_id", nullable = false)
    private Member mentionedMember;

    @Builder(access = AccessLevel.PRIVATE)
    private ChatMessageMention(ChatMessage chatMessage, Member mentionedMember) {
        this.chatMessage = chatMessage;
        this.mentionedMember = mentionedMember;
    }

    public static ChatMessageMention createChatMessageMention(ChatMessage chatMessage, Member mentionedMember) {
        return ChatMessageMention.builder()
                .chatMessage(chatMessage)
                .mentionedMember(mentionedMember)
                .build();
    }
}
