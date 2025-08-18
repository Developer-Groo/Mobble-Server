package com.mobble.mobbleserver.domain.chat.chatRoomParticipant.service;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.domain.chat.chatMessage.validator.ChatMessageValidator;
import com.mobble.mobbleserver.domain.chat.chatRoomParticipant.entity.ChatRoomParticipant;
import com.mobble.mobbleserver.domain.chat.chatRoomParticipant.validator.ChatRoomParticipantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomParticipantService {

    private final ChatRoomParticipantValidator chatRoomParticipantValidator;
    private final ChatMessageValidator chatMessageValidator;

    @Transactional
    public void updateLastReadMessage(Long memberId, Long chatRoomId, Long lastMessageId) {
        ChatRoomParticipant participant = chatRoomParticipantValidator.findParticipantByChatRoomIdAndMemberIdOrThrow(chatRoomId, memberId);
        ChatMessage lastReadMessage = chatMessageValidator.findChatMessageByChatMessageIdOrThrow(lastMessageId);

        participant.updateLastReadMessage(lastReadMessage);
    }

    @Transactional
    public void updateNotificationStatus(Long chatRoomId, Long memberId, boolean enabled) {
        ChatRoomParticipant participant = chatRoomParticipantValidator.findParticipantByChatRoomIdAndMemberIdOrThrow(chatRoomId, memberId);

        if (enabled) {
            participant.enableNotified();
        } else {
            participant.disableNotified();
        }
    }
}
