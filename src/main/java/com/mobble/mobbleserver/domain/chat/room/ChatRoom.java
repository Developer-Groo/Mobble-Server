package com.mobble.mobbleserver.domain.chat.room;

import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.common.CreatedAtEntity;
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

    @OneToOne(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private RoomInfo roomInfo;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private ChatRoom(ChatRoomType type) {
        this.type = type;
    }

    public static ChatRoom createDirect(Member memberA, Member memberB) {
        ChatRoom room = ChatRoom.builder()
                .type(ChatRoomType.DIRECT)
                .build();
        room.attachDirectInfo(memberA, memberB);

        return room;
    }

    public static ChatRoom createClub(Club club) {
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

        if (alreadyJoined) throw new IllegalStateException(""); // Todo: Error 수정

        Participant participant = Participant.create(this, member);
        participants.add(participant);
    }

    public void removeParticipant(Member member) {
        participants.removeIf(participant -> {
            boolean equals = participant.getMember().getId().equals(member.getId());
            if (equals) participant.detach();
            return equals;
        });
    }

    public boolean hasParticipant(Member member) {
        return participants.stream()
                .anyMatch(participant -> participant.getMember().getId().equals(member.getId()));
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

    private Participant findParticipant(Member member) {
        return participants.stream()
                .filter(participant -> participant.getMember().getId().equals(member.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("")); // Todo: Error 수정
    }

    /* Info 관리 */
    private void attachDirectInfo(Member a, Member b) {
        assertType(ChatRoomType.DIRECT);
        this.roomInfo = DirectRoomInfo.create(this, a, b);
    }

    private void attachClubInfo(Club club) {
        assertType(ChatRoomType.GROUP);
        this.roomInfo = ClubRoomInfo.create(this, club);
    }

    private void assertType(ChatRoomType expected) {
        if (this.type != expected) {
            throw new IllegalStateException("채팅방 타입이 일치하지 않습니다."); // Todo: Error 수정
        }
    }

    public Club getClub() {
        if (roomInfo instanceof ClubRoomInfo clubRoomInfo) {
            return clubRoomInfo.getClub();
        }
        return null;
    }

    /* 1대1 채팅방 전용 */
    public Member getReceiverFor(Long senderId) {
        if (this.type != ChatRoomType.DIRECT) throw new IllegalStateException(""); // Todo: Error 수정

        DirectRoomInfo directRoomInfo = (DirectRoomInfo) this.roomInfo;

        return directRoomInfo.getReceiverFor(senderId);
    }
}
