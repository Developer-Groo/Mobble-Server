package com.mobble.mobbleserver.domain.chat.chatRoomParticipant.controller;

import com.mobble.mobbleserver.domain.chat.chatRoomParticipant.service.ChatRoomParticipantService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomParticipantController {

    private final ChatRoomParticipantService chatRoomParticipantService;

    @PatchMapping("/chat-rooms/{chat-room-id}/read")
    public ResponseEntity<Void> updateLastReadMessage(
            @PathVariable("chat-room-id") @Positive Long chatRoomId,
            @RequestParam("message-id") @Positive Long messageId
    ) {
        Long memberId = 1L;
        chatRoomParticipantService.updateLastReadMessage(memberId, chatRoomId, messageId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
