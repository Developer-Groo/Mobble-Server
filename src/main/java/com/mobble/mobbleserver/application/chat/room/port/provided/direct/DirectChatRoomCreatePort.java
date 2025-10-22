package com.mobble.mobbleserver.application.chat.room.port.provided.direct;

import com.mobble.mobbleserver.infrastructure.web.chat.room.direct.dto.request.DirectChatRoomCreateRequestDto;
import com.mobble.mobbleserver.infrastructure.web.chat.room.direct.dto.response.DirectChatRoomPreviewResponseDto;

public interface DirectChatRoomCreatePort {

    DirectChatRoomPreviewResponseDto createDirectChatRoom(DirectChatRoomCreateRequestDto dto, Long memberId);
}
