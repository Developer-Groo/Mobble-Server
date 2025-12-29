package com.mobble.mobbleserver.domain.chat.room;

import com.mobble.mobbleserver.domain.chat.room.error.ChatRoomError;
import com.mobble.mobbleserver.domain.exception.DomainException;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "direct_room_info",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_direct_room_member_pair",
                        columnNames = {"member_a_id", "member_b_id"}
                )
        }
)
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
        if (memberA.getId().equals(memberB.getId())) throw new DomainException(ChatRoomError.SELF_DIRECT_CHAT_NOT_ALLOWED);

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
