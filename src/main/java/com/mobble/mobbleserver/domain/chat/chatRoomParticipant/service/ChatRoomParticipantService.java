package com.mobble.mobbleserver.domain.chat.chatRoomParticipant.service;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.domain.chat.chatMessage.validator.ChatMessageValidator;
import com.mobble.mobbleserver.domain.chat.chatRoomParticipant.entity.ChatRoomParticipant;
import com.mobble.mobbleserver.domain.chat.chatRoomParticipant.validator.ChatRoomParticipantValidator;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomParticipantService {

    private final ChatRoomParticipantValidator chatRoomParticipantValidator;
    private final ChatMessageValidator chatMessageValidator;
    private final MemberValidator memberValidator;

    @Transactional
    public void updateLastReadMessage(Long memberId, Long chatRoomId, Long lastMessageId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        ChatRoomParticipant participant = chatRoomParticipantValidator.findParticipantByChatRoomIdAndMemberIdOrThrow(chatRoomId, member.getId());

        ChatMessage lastReadMessage = chatMessageValidator.findChatMessageByChatMessageId(lastMessageId);

        participant.updateLastReadMessage(lastReadMessage);
    }
}
