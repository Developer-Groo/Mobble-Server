package com.mobble.mobbleserver.refactor.chat.clubChatRoom.validator;

import com.mobble.mobbleserver.refactor.chat.clubChatRoom.entity.ClubChatRoom;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.repository.ClubChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubChatRoomValidator {

    private final ClubChatRoomRepository clubChatRoomRepository;

    public void existsClubChatRoomByClubIdOrThrow(Long clubId) {
        if (clubChatRoomRepository.existsClubChatRoomByClubId(clubId)) throw new IllegalArgumentException("");
    }

    public ClubChatRoom findClubChatRoomByClubIdOrThrow(Long clubId) {
        return clubChatRoomRepository.findByClubId(clubId)
                .orElseThrow(() -> new IllegalArgumentException(""));
    }
}
