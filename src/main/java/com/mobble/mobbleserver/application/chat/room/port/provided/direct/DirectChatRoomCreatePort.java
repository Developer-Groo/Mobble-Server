package com.mobble.mobbleserver.application.chat.room.port.provided.direct;

import com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.request.DirectChatRoomCreateRequestDto;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.response.DirectChatRoomPreviewResponseDto;

public interface DirectChatRoomCreatePort {

    DirectChatRoomPreviewResponseDto createDirectChatRoom(DirectChatRoomCreateRequestDto dto, Long memberId);
}
