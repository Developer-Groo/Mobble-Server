package com.mobble.mobbleserver.domain.chat.clubChatRoom.validator;

import com.mobble.mobbleserver.domain.chat.clubChatRoom.repository.ClubChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubChatRoomValidator {

    private final ClubChatRoomRepository clubChatRoomRepository;

    public void existsClubChatRoomByClubId(Long clubId) {
        if (clubChatRoomRepository.existsClubChatRoomByClubId(clubId)) {
            throw new IllegalArgumentException("");
        }
    }
}
