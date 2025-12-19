package com.mobble.mobbleserver.domain.chat.message;

import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.common.CreatedAtEntity;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MessageType type;

    @OneToMany(mappedBy = "chatMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageMention> mentions = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private ChatMessage(
            ChatRoom chatRoom,
            Member sender,
            String content,
            MessageType type
    ) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.type = type;
    }

    public static ChatMessage create(
            ChatRoom chatRoom,
            Member sender,
            String content,
            MessageType type,
            List<Long> mentionIds
    ) {
        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .type(type)
                .build();

        message.setMentions(mentionIds);

        return message;
    }

    public List<Long> getMentionedMemberIds() {
        return mentions.stream()
                .map(ChatMessageMention::getMentionedMemberId)
                .toList();
    }

    private void setMentions(List<Long> memberIds) {
        this.mentions.clear();

        if (memberIds == null || memberIds.isEmpty()) return;

        memberIds.stream()
                .distinct()
                .forEach(id ->
                        this.mentions.add(ChatMessageMention.create(this, id))
                );
    }
}
