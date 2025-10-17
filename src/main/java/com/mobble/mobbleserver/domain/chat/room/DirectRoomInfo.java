package com.mobble.mobbleserver.domain.chat.room;

import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@PrimaryKeyJoinColumn(name = "room_info_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DirectRoomInfo extends RoomInfo {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_a_id", nullable = false)
    private Member memberA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_b_id", nullable = false)
    private Member memberB;

    @Builder(access = AccessLevel.PRIVATE)
    private DirectRoomInfo(ChatRoom chatRoom, Member memberA, Member memberB) {
        super.setChatRoom(chatRoom);
        this.memberA = memberA;
        this.memberB = memberB;
    }

    static DirectRoomInfo create(ChatRoom chatRoom, Member memberA, Member memberB) {
        Member a = memberA.getId() < memberB.getId() ? memberA : memberB;
        Member b = memberA.getId() < memberB.getId() ? memberB : memberA;

        return DirectRoomInfo.builder()
                .chatRoom(chatRoom)
                .memberA(a)
                .memberB(b)
                .build();
    }

    Member getReceiverFor(Long senderId) {
        return this.memberA.getId().equals(senderId) ? memberB : memberA;
    }
}
