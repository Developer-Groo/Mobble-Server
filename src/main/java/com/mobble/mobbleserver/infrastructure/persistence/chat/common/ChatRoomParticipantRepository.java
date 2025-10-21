package com.mobble.mobbleserver.infrastructure.persistence.chat.common;

import com.mobble.mobbleserver.domain.chat.room.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomParticipantRepository extends JpaRepository<Participant, Long>, ChatRoomParticipantQueryRepository {

    Optional<Participant> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    boolean existsByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    long countByChatRoomId(Long id);
}
