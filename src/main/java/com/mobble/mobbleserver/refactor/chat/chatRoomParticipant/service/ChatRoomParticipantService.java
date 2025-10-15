package com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.service;

import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberValidationErrorCode;
import com.mobble.mobbleserver.refactor.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.refactor.chat.chatMessage.validator.ChatMessageValidator;
import com.mobble.mobbleserver.domain.chat.room.ChatRoomParticipant;
import com.mobble.mobbleserver.refactor.chat.chatRoom.validator.ChatRoomValidator;
import com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.validator.ChatRoomParticipantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomParticipantService {

    private final ChatRoomValidator chatRoomValidator;
    private final MemberReadPort memberReadPort;

    @Transactional
    public void updateLastReadMessage(Long memberId, Long chatRoomId, Long lastMessageId) {
        Member member = memberReadPort.findByIdAndIsDeletedFalse(memberId).orElseThrow();
        ChatRoom chatRoom = chatRoomValidator.findChatRoomByChatRoomIdOrThrow(chatRoomId);

        chatRoom.updateLastReadMessage(member, lastMessageId);
    }

    @Transactional
    public void updateNotificationStatus(Long chatRoomId, Long memberId, boolean enabled) {
        Member member = memberReadPort.findByIdAndIsDeletedFalse(memberId).orElseThrow();
        ChatRoom chatRoom = chatRoomValidator.findChatRoomByChatRoomIdOrThrow(chatRoomId);

        if (enabled) {
            chatRoom.enableNotified(member);
        } else {
            chatRoom.disableNotified(member);
        }
    }
}
