package com.mobble.mobbleserver.domain.chat.room;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "room_kind")
abstract class RoomInfo {

    @Id
    @Column(name = "room_info_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false, unique = true)
    private ChatRoom chatRoom;

    protected void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
