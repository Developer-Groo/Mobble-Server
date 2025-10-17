package com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.repository;

import com.mobble.mobbleserver.domain.chat.room.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomParticipantRepository extends JpaRepository<Participant, Long>, ChatRoomParticipantQueryRepository {

    Optional<Participant> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    boolean existsByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    void deleteByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    void deleteByChatRoomId(Long chatRoomId);

    long countByChatRoomId(Long id);
}
