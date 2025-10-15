package com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.repository;

import com.mobble.mobbleserver.domain.chat.room.ChatRoomParticipant;

import java.util.List;

public interface ChatRoomParticipantQueryRepository {

    List<ChatRoomParticipant> findAllByChatRoomIdsAndMemberId(List<Long> chatRoomId, Long memberId);
}
