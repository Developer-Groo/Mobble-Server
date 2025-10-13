package com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.controller;

import com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.service.ChatRoomParticipantService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-rooms/{chat-room-id}")
public class ChatRoomParticipantController {

    private final ChatRoomParticipantService chatRoomParticipantService;

    @PatchMapping("/read")
    public ResponseEntity<Void> updateLastReadMessage(
            @PathVariable("chat-room-id") @Positive Long chatRoomId,
            @RequestParam("message-id") @Positive Long messageId
    ) {
        Long memberId = 1L;
        chatRoomParticipantService.updateLastReadMessage(memberId, chatRoomId, messageId);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @PatchMapping("/notification")
    public ResponseEntity<Void> updateNotification(
            @PathVariable("chat-room-id") @Positive Long chatRoomId,
            @RequestParam("enabled") @Positive boolean enabled
    ) {
        Long memberId = 1L;
        chatRoomParticipantService.updateNotificationStatus(chatRoomId, memberId, enabled);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
