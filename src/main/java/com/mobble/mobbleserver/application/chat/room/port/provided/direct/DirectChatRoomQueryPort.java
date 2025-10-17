package com.mobble.mobbleserver.application.chat.room.port.provided.direct;

import com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.response.DirectChatRoomPreviewResponseDto;

import java.util.List;

public interface DirectChatRoomQueryPort {

    List<DirectChatRoomPreviewResponseDto> getDirectChatRoomsPreview(Long memberId);
}
