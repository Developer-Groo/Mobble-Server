package com.mobble.mobbleserver.refactor.chat.directChatRoom.entity;

import com.mobble.mobbleserver.common.baseEntity.CreatedAtEntity;
import com.mobble.mobbleserver.refactor.chat.chatRoom.entity.ChatRoom;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DirectChatRoom extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "direct_chat_room_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false, unique = true)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_a_id", nullable = false)
    private Member memberA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_b_id", nullable = false)
    private Member memberB;

    @Builder(access = AccessLevel.PRIVATE)
    private DirectChatRoom(ChatRoom chatRoom, Member memberA, Member memberB) {
        this.chatRoom = chatRoom;
        this.memberA = memberA;
        this.memberB = memberB;
    }

    public static DirectChatRoom createDirectChatRoom(ChatRoom chatRoom, Member memberA, Member memberB) {
        Member a = memberA.getId() < memberB.getId() ? memberA : memberB;
        Member b = memberA.getId() < memberB.getId() ? memberB : memberA;

        return DirectChatRoom.builder()
                .chatRoom(chatRoom)
                .memberA(a)
                .memberB(b)
                .build();
    }
}
