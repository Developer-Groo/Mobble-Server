package com.mobble.mobbleserver.domain.chat.chatMessage.service;

import com.mobble.mobbleserver.domain.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.domain.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.domain.chat.chatMessage.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessageResponseDto> getMessagesForParticipant(
            Long chatRoomId,
            LocalDateTime joinedAt,
            Long lastMessageId,
            LocalDateTime lastCreatedAt
    ) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maxPeriod = now.minusDays(30);
        LocalDateTime startDate = joinedAt.isAfter(maxPeriod) ? joinedAt : maxPeriod;

        List<ChatMessage> messages = chatMessageRepository.findMessagesFrom(chatRoomId, startDate, lastMessageId, lastCreatedAt);

        return messages.stream()
                .map(ChatMessageResponseDto::toDto)
                .toList();
    }
}
