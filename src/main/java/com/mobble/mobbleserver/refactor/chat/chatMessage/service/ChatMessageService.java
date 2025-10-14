package com.mobble.mobbleserver.refactor.chat.chatMessage.service;

import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.request.ChatMessageRequestDto;
import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.refactor.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.refactor.chat.chatMessage.entity.MessageType;
import com.mobble.mobbleserver.refactor.chat.chatMessage.repository.ChatMessageRepository;
import com.mobble.mobbleserver.refactor.chat.chatRoom.entity.ChatRoom;
import com.mobble.mobbleserver.refactor.chat.chatRoom.validator.ChatRoomValidator;
import com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.entity.ChatRoomParticipant;
import com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.validator.ChatRoomParticipantValidator;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatRoomParticipantValidator chatRoomParticipantValidator;
    private final ChatRoomValidator chatRoomValidator;

    private final ChatMessageRepository chatMessageRepository;

    private final MemberReadPort memberReadPort;

    @Transactional
    public ChatMessage saveChatMessageAndUpdateLastRead(
            Long chatRoomId,
            Long senderId,
            String content,
            MessageType messageType
    ) {
        Member sender = findMemberByMemberIdOrThrow(senderId);
        ChatRoom chatRoom = chatRoomValidator.findChatRoomByChatRoomIdOrThrow(chatRoomId);

        ChatMessage chatMessage = ChatMessage.createChatMessage(chatRoom, sender, content, messageType);
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        ChatRoomParticipant participant = chatRoomParticipantValidator.findParticipantByChatRoomIdAndMemberIdOrThrow(chatRoom.getId(), sender.getId());
        participant.updateLastReadMessage(savedMessage);

        return savedMessage;
    }

    public List<ChatMessageResponseDto> getMessagesForParticipant(
            Long memberId,
            ChatMessageRequestDto dto
    ) {
        ChatRoomParticipant participant = chatRoomParticipantValidator.findParticipantByChatRoomIdAndMemberIdOrThrow(dto.chatRoomId(), memberId);
        Long chatRoomId = participant.getChatRoom().getId();
        LocalDateTime joinedAt = participant.getJoinedAt();

        Long lastMessageId = dto.lastMessageId();
        LocalDateTime lastCreatedAt = dto.lastCreatedAt();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maxPeriod = now.minusDays(30);
        LocalDateTime startDate = joinedAt.isAfter(maxPeriod) ? joinedAt : maxPeriod;

        List<ChatMessage> messages = chatMessageRepository.findMessagesFrom(chatRoomId, startDate, lastMessageId, lastCreatedAt);

        return messages.stream()
                .map(ChatMessageResponseDto::toDto)
                .toList();
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
