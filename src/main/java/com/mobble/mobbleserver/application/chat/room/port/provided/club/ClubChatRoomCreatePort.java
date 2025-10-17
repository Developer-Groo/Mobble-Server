package com.mobble.mobbleserver.application.chat.room.port.provided.club;

import com.mobble.mobbleserver.refactor.chat.clubChatRoom.dto.response.ClubChatRoomPreviewResponseDto;

public interface ClubChatRoomCreatePort {

    ClubChatRoomPreviewResponseDto createClubChatRoom(Long clubId, Long memberId);
}
