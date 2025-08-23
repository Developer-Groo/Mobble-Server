package com.mobble.mobbleserver.domain.chat.directChatRoom.validator;

import com.mobble.mobbleserver.domain.chat.directChatRoom.repository.DirectChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DirectChatRoomValidator {

    private final DirectChatRoomRepository directChatRoomRepository;

    public void existsDirectChatRoomByBetweenMembersOrThrow(Long senderId, Long receiverId) {
        if (directChatRoomRepository.existsDirectChatRoomByBetweenMembers(senderId, receiverId)) throw new IllegalArgumentException("");
    }
}
