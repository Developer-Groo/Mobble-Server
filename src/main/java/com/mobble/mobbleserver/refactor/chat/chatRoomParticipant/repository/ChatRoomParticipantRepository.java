package com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.repository;

import com.mobble.mobbleserver.domain.chat.room.ChatRoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, Long>, ChatRoomParticipantQueryRepository {

    Optional<ChatRoomParticipant> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    boolean existsByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    void deleteByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    void deleteByChatRoomId(Long chatRoomId);
}
