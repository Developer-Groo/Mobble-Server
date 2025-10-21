package com.mobble.mobbleserver.refactor.chat.chatMessage.service;

import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.message.MessageType;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.request.ChatMessageRequestDto;
import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.refactor.chat.chatMessage.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatRoomReadPort chatRoomReadPort;

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
        ChatRoom chatRoom = chatRoomReadPort.findChatRoomById(chatRoomId).orElseThrow();

        ChatMessage chatMessage = ChatMessage.createChatMessage(chatRoom, sender, content, messageType);
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        chatRoom.updateLastReadMessage(sender, savedMessage.getId());

        return savedMessage;
    }

    public List<ChatMessageResponseDto> getMessagesForParticipant(
            Long memberId,
            ChatMessageRequestDto dto
    ) {
        Member member = memberReadPort.findByIdAndIsDeletedFalse(memberId).orElseThrow();
        Long chatRoomId = dto.chatRoomId();

        ChatRoom chatRoom = chatRoomReadPort.findChatRoomById(chatRoomId).orElseThrow();
        LocalDateTime joinedAt = chatRoom.joinedAtOf(member);

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
