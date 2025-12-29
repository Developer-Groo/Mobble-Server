package com.mobble.mobbleserver.application.chat.message.result;

import com.mobble.mobbleserver.domain.chat.message.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageSliceResult(
        boolean hasMore,
        Long nextCursorId,
        LocalDateTime nextCursorCreatedAt,
        List<ChatMessageResult> results
) {

    public static ChatMessageSliceResult create(List<ChatMessage> fetched, int limit) {
        if (fetched == null || fetched.isEmpty()) return empty();

        boolean hasMore = fetched.size() > limit;
        List<ChatMessage> page = hasMore ? fetched.subList(0, limit) : fetched;

        List<ChatMessageResult> results = page.stream()
                .map(ChatMessageResult::create)
                .toList();

        ChatMessage last = page.get(page.size() - 1);

        return new ChatMessageSliceResult(
                hasMore,
                last.getId(),
                last.getCreatedAt(),
                results
        );
    }

    public static ChatMessageSliceResult empty() {
        return new ChatMessageSliceResult(false, null, null, List.of());
    }
}
