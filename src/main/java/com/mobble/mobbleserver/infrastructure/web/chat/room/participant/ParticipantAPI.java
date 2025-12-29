package com.mobble.mobbleserver.infrastructure.web.chat.room.participant;

import com.mobble.mobbleserver.application.chat.room.port.provided.command.participant.ParticipantUpdatePort;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/rooms/{chat-room-id}")
public class ParticipantAPI {

    private final ParticipantUpdatePort participantUpdatePort;

    @PatchMapping("/read")
    public ResponseEntity<Void> updateLastReadMessage(
            @PathVariable("chat-room-id") @Positive Long chatRoomId,
            @RequestParam("message-id") @Positive Long messageId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        participantUpdatePort.updateLastReadMessage(chatRoomId, memberId, messageId);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @PatchMapping("/notification")
    public ResponseEntity<Void> updateNotification(
            @PathVariable("chat-room-id") @Positive Long chatRoomId,
            @RequestParam("enabled") @Positive boolean enabled,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        participantUpdatePort.updateNotification(chatRoomId, memberId, enabled);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
