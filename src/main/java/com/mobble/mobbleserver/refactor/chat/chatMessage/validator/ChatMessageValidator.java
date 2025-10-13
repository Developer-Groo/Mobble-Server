package com.mobble.mobbleserver.refactor.chat.chatMessage.validator;

import com.mobble.mobbleserver.refactor.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.refactor.chat.chatMessage.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageValidator {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage findChatMessageByChatMessageIdOrThrow(Long chatMessageId) {
        return chatMessageRepository.findById(chatMessageId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: ErrorCode 적용
    }
}
