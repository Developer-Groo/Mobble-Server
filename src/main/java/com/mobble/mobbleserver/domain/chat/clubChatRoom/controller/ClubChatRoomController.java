package com.mobble.mobbleserver.domain.chat.clubChatRoom.controller;

import com.mobble.mobbleserver.domain.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.request.ClubChatRoomRequestDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.service.ClubChatRoomService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClubChatRoomController {

    private final ClubChatRoomService clubChatRoomService;

    @MessageMapping("/group/chat/send")
    public void handleMessage(ClubChatRoomRequestDto dto) {
        Long memberId = 1L;

        clubChatRoomService.sendGroupMessage(dto, memberId);
    }

    @GetMapping("/chat-rooms/clubs")
    public ResponseEntity<List<ClubChatRoomPreviewResponseDto>> getClubChatRooms() {
        Long memberId = 1L;

        return ResponseEntity.status(HttpStatus.OK)
                .body(clubChatRoomService.getClubChatRooms(memberId));
    }

    @GetMapping("/clubs/{club-id}/chat-rooms/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getClubChatRoomMessages(
            @PathVariable(name = "club-id") @Positive Long clubId,
            @RequestParam(name = "last-message-id", required = false) @Positive Long lastMessageId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clubChatRoomService.getClubChatRoomMessages(clubId, lastMessageId));
    }

    @PatchMapping("/clubs/{club-id}/chat-rooms/read")
    public ResponseEntity<Void> updateLastReadMessage(
            @PathVariable(name = "club-id") @Positive Long clubId,
            @RequestParam(name = "message-id") @Positive Long messageId
    ) {
        Long memberId = 1L;
        clubChatRoomService.updateLastReadMessage(clubId, memberId, messageId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
