package com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.repository;

import com.mobble.mobbleserver.domain.chat.room.Participant;

import java.util.List;

public interface ChatRoomParticipantQueryRepository {

    List<Participant> findAllByChatRoomIdsAndMemberId(List<Long> chatRoomId, Long memberId);
}
