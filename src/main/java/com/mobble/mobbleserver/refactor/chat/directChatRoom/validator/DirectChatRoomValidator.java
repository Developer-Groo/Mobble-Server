package com.mobble.mobbleserver.refactor.chat.directChatRoom.validator;

import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;
import com.mobble.mobbleserver.infrastructure.persistence.chat.direct.DirectChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DirectChatRoomValidator {

    private final DirectChatRoomRepository directChatRoomRepository;

    public List<DirectRoomInfo> findDirectChatRoomsAllByMemberId(Long memberId) {
        return directChatRoomRepository.findDirectChatRoomsAllByMemberId(memberId);
    }

    public void existsDirectChatRoomByBetweenMembersOrThrow(Long senderId, Long receiverId) {
        if (directChatRoomRepository.existsDirectChatRoomByBetweenMembers(senderId, receiverId)) throw new IllegalArgumentException("");
    }
}
