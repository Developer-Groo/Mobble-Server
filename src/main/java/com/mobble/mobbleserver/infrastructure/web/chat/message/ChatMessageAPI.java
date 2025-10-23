package com.mobble.mobbleserver.infrastructure.web.chat.message;

import com.mobble.mobbleserver.application.chat.message.port.provided.MessageQueryPort;
import com.mobble.mobbleserver.infrastructure.web.chat.message.dto.response.ChatMessageResponseDto;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatMessageAPI {

    private final MessageQueryPort messageQueryPort;

    @GetMapping("/chat-rooms/{chat-room-id}/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> list(
            @PathVariable(name = "chat-room-id") @Positive Long chatRoomId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false) LocalDateTime lastCreatedAt,
            @RequestParam(defaultValue = "prev") String direction,
            @RequestParam(defaultValue = "50") int limit,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(messageQueryPort.list(chatRoomId, memberId, cursor, lastCreatedAt, direction, limit));
    }
}
