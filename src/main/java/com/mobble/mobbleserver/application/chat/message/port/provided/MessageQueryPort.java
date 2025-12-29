package com.mobble.mobbleserver.application.chat.message.port.provided;

import com.mobble.mobbleserver.application.chat.message.result.ChatMessageSliceResult;

import java.time.LocalDateTime;

public interface MessageQueryPort {

    ChatMessageSliceResult getMessages(Long chatRoomId, Long memberId, Long cursorId, LocalDateTime cursorCreatedAt);
}
