package com.mobble.mobbleserver.domain.chat.chatRoomParticipant.repository;

import com.mobble.mobbleserver.domain.chat.chatRoomParticipant.entity.ChatRoomParticipant;

import java.util.List;

public interface ChatRoomParticipantQueryRepository {

    List<ChatRoomParticipant> findAllByChatRoomIdsAndMemberId(List<Long> chatRoomId, Long memberId);
}
