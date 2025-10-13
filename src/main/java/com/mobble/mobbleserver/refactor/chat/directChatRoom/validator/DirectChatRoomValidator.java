package com.mobble.mobbleserver.refactor.chat.directChatRoom.validator;

import com.mobble.mobbleserver.refactor.chat.directChatRoom.entity.DirectChatRoom;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.repository.DirectChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DirectChatRoomValidator {

    private final DirectChatRoomRepository directChatRoomRepository;

    public List<DirectChatRoom> findDirectChatRoomsAllByMemberId(Long memberId) {
        return directChatRoomRepository.findDirectChatRoomsAllByMemberId(memberId);
    }

    public void existsDirectChatRoomByBetweenMembersOrThrow(Long senderId, Long receiverId) {
        if (directChatRoomRepository.existsDirectChatRoomByBetweenMembers(senderId, receiverId)) throw new IllegalArgumentException("");
    }
}
