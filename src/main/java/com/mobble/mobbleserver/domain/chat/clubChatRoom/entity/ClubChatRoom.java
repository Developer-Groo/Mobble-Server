package com.mobble.mobbleserver.domain.chat.clubChatRoom.entity;

import com.mobble.mobbleserver.domain.chat.chatRoom.entity.ChatRoom;
import com.mobble.mobbleserver.domain.club.entity.Club;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubChatRoom {

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
    public ClubChatRoom(Club club, ChatRoom chatRoom) {
        this.club = club;
        this.chatRoom = chatRoom;
    }

    public static ClubChatRoom createClubChatRoom(Club club, ChatRoom chatRoom) {
        return ClubChatRoom.builder()
                .club(club)
                .chatRoom(chatRoom)
                .build();
    }
}
