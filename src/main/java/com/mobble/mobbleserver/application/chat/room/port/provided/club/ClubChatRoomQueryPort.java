package com.mobble.mobbleserver.application.chat.room.port.provided.club;

import com.mobble.mobbleserver.refactor.chat.clubChatRoom.dto.response.ClubChatRoomPreviewResponseDto;

import java.util.List;

public interface ClubChatRoomQueryPort {

    List<ClubChatRoomPreviewResponseDto> getClubChatRoomsPreview(Long memberId);
}
