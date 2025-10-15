package com.mobble.mobbleserver.domain.chat.room;

import com.mobble.mobbleserver.common.baseEntity.CreatedAtEntity;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomParticipant> participants = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private ChatRoom(ChatRoomType type) {
        this.type = type;
    }

    public static ChatRoom createChatRoom(ChatRoomType type) {
        return ChatRoom.builder()
                .type(type)
                .build();
    }

    public ChatRoomParticipant addParticipant(Member member) {
        boolean alreadyJoined = participants.stream()
                .anyMatch(participant -> participant.getMember().getId().equals(member.getId()));

        if (alreadyJoined) throw new IllegalStateException("");

        ChatRoomParticipant participant = ChatRoomParticipant.createChatRoomParticipant(this, member);
        participants.add(participant);

        return participant;
    }

    public void removeParticipant(Member member) {
        participants.removeIf(participant -> participant.getMember().getId().equals(member.getId()));
    }

    public void enableNotified(Member member) {
        findParticipant(member).enableNotified();
    }

    public void disableNotified(Member member) {
        findParticipant(member).disableNotified();
    }

    public void updateLastReadMessage(Member member, Long messageId) {
        findParticipant(member).updateLastReadMessage(messageId);
    }

    public LocalDateTime joinedAtOf(Member member) {
        return findParticipant(member).getJoinedAt();
    }

    private ChatRoomParticipant findParticipant(Member member) {
        return participants.stream()
                .filter(participant -> participant.getMember().getId().equals(member.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(""));
    }
}
