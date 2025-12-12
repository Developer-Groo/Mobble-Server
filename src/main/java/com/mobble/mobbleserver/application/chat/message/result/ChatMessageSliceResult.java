package com.mobble.mobbleserver.application.chat.message.result;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageSliceResult(
        boolean hasMore,
        Long nextCursorId,
        LocalDateTime nextCursorCreatedAt,
        List<ChatMessageResult> results
) {

    public static ChatMessageSliceResult create(
            boolean hasMore,
            Long nextCursorId,
            LocalDateTime nextCursorCreatedAt,
            List<ChatMessageResult> results
    ) {
        return new ChatMessageSliceResult(
                hasMore,
                nextCursorId,
                nextCursorCreatedAt,
                results
        );
    }

    public static ChatMessageSliceResult empty() {
        return new ChatMessageSliceResult(false, null, null, List.of());
    }
}
