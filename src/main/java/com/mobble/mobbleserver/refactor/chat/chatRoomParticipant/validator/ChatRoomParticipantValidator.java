package com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.validator;

import com.mobble.mobbleserver.domain.chat.room.Participant;
import com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.repository.ChatRoomParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomParticipantValidator {

    private final ChatRoomParticipantRepository chatRoomParticipantRepository;

    public Participant findParticipantByChatRoomIdAndMemberIdOrThrow(Long chatRoomId, Long memberId) {
        return chatRoomParticipantRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: ErrorCode 적용
    }
}
