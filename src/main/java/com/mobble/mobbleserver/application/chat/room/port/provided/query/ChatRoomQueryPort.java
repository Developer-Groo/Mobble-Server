package com.mobble.mobbleserver.application.chat.room.port.provided.query;

import com.mobble.mobbleserver.application.chat.room.result.ChatRoomPreviewResult;
import com.mobble.mobbleserver.application.chat.room.service.query.ChatRoomPreviewFilter;

import java.util.List;

public interface ChatRoomQueryPort {

    List<ChatRoomPreviewResult> getChatRoomsPreview(Long memberId, ChatRoomPreviewFilter filter);
}
