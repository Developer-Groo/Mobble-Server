package com.mobble.mobbleserver.domain.chat.room;

import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@PrimaryKeyJoinColumn(name = "room_info_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubRoomInfo extends RoomInfo {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false, unique = true)
    private Club club;

    @Builder(access = AccessLevel.PRIVATE)
    private ClubRoomInfo(ChatRoom chatRoom, Club club) {
        super.setChatRoom(chatRoom);
        this.club = club;
    }

    static ClubRoomInfo create(ChatRoom chatRoom, Club club) {
        return ClubRoomInfo.builder()
                .chatRoom(chatRoom)
                .club(club)
                .build();
    }
}
