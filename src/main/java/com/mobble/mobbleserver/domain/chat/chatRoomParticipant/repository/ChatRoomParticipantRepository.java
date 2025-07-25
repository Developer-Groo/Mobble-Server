package com.mobble.mobbleserver.domain.chat.chatRoomParticipant.repository;

import com.mobble.mobbleserver.domain.chat.chatRoomParticipant.entity.ChatRoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, Long>, ChatRoomParticipantQueryRepository {

    Optional<ChatRoomParticipant> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
}
