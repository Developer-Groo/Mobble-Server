package com.mobble.mobbleserver.domain.chat.room;

import com.mobble.mobbleserver.common.baseEntity.CreatedAtEntity;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.club.core.entity.Club;
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

    @OneToOne(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private RoomInfo chatRoomInfo;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomParticipant> participants = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private ChatRoom(ChatRoomType type) {
        this.type = type;
    }

    public static ChatRoom createDirectChatRoom(Member memberA, Member memberB) {
        ChatRoom room = ChatRoom.builder()
                .type(ChatRoomType.DIRECT)
                .build();
        room.attachDirectInfo(memberA, memberB);

        return room;
    }

    public static ChatRoom createClubChatRoom(Club club) {
        ChatRoom room = ChatRoom.builder()
                .type(ChatRoomType.GROUP)
                .build();
        room.attachClubInfo(club);

        return room;
    }

    /* Participant 관리 */
    public void addParticipant(Member member) {
        boolean alreadyJoined = participants.stream()
                .anyMatch(participant -> participant.getMember().getId().equals(member.getId()));

        if (alreadyJoined) throw new IllegalStateException("");

        ChatRoomParticipant participant = ChatRoomParticipant.createChatRoomParticipant(this, member);
        participants.add(participant);
    }

    public void removeParticipant(Member member) {
        participants.removeIf(participant -> {
            boolean equals = participant.getMember().getId().equals(member.getId());
            if (equals) participant.detach();
            return equals;
        });
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

    /* Info 관리 */
    private void attachDirectInfo(Member a, Member b) {
        assertType(ChatRoomType.DIRECT);
        this.chatRoomInfo = DirectRoomInfo.create(this, a, b);
    }

    private void attachClubInfo(Club club) {
        assertType(ChatRoomType.GROUP);
        this.chatRoomInfo = ClubRoomInfo.create(this, club);
    }

    private void assertType(ChatRoomType expected) {
        if (this.type != expected) {
            throw new IllegalStateException("채팅방 타입이 일치하지 않습니다.");
        }
    }

    /* 1대1 채팅방 전용 */
    public Member getReceiverFor(Long senderId) {
        if (this.type != ChatRoomType.DIRECT) throw new IllegalStateException("");

        DirectRoomInfo directRoomInfo = (DirectRoomInfo) this.chatRoomInfo;

        return directRoomInfo.getReceiverFor(senderId);
    }
}
