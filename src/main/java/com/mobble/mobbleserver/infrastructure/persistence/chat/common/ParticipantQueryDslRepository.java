package com.mobble.mobbleserver.infrastructure.persistence.chat.common;

import com.mobble.mobbleserver.domain.chat.room.Participant;

import java.util.List;

public interface ParticipantQueryDslRepository {

    List<Participant> findAllByChatRoomIdsAndMemberId(List<Long> chatRoomId, Long memberId);
}
