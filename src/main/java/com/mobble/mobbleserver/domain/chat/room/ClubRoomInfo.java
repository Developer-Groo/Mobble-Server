package com.mobble.mobbleserver.domain.chat.room;

import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubRoomInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_chat_room_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false, unique = true)
    private Club club;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false, unique = true)
    private ChatRoom chatRoom;

    @Builder(access = AccessLevel.PRIVATE)
    private ClubRoomInfo(Club club, ChatRoom chatRoom) {
        this.club = club;
        this.chatRoom = chatRoom;
    }

    public static ClubRoomInfo createClubChatRoom(Club club, ChatRoom chatRoom) {
        return ClubRoomInfo.builder()
                .club(club)
                .chatRoom(chatRoom)
                .build();
    }

    public void attachTo(Club club) {
        if (this.club != club) throw new IllegalArgumentException("");

        club.setClubChatRoomInternal(this);
    }

    public void detach() {
        if (this.club == null) return;

        this.club.setClubChatRoomInternal(null);
        this.club = null;
    }
}
