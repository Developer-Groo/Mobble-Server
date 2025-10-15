package com.mobble.mobbleserver.refactor.chat.chatRoom.validator;

import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.refactor.chat.chatRoom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomValidator {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom findChatRoomByChatRoomIdOrThrow(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException(""));
    }
}
