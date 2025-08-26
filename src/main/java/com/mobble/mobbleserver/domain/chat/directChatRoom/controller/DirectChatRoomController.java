package com.mobble.mobbleserver.domain.chat.directChatRoom.controller;

import com.mobble.mobbleserver.domain.chat.chatMessage.dto.request.ChatMessageRequestDto;
import com.mobble.mobbleserver.domain.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.request.DirectChatMessageRequestDto;
import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.request.DirectChatRoomCreateRequestDto;
import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.response.DirectChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.domain.chat.directChatRoom.service.DirectChatRoomService;
import jakarta.validation.Valid;
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
public class DirectChatRoomController {

    private final DirectChatRoomService directChatRoomService;

    @MessageMapping("/direct/chat/send")
    public void handleMessage(
            @RequestBody DirectChatMessageRequestDto dto
    ) {
        Long memberId = 1L;

        directChatRoomService.sendDirectMessage(dto, memberId);
    }

    @PostMapping("/chat-rooms/direct")
    public ResponseEntity<DirectChatRoomPreviewResponseDto> createDirectChatRoom(
            @RequestBody DirectChatRoomCreateRequestDto dto
    ) {
        Long memberId = 1L;

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(directChatRoomService.createDirectChatRoom(dto, memberId));
    }

    @GetMapping("/chat-rooms/direct")
    public ResponseEntity<List<DirectChatRoomPreviewResponseDto>> getDirectChatRooms() {
        Long memberId = 1L;

        return ResponseEntity.status(HttpStatus.OK)
                .body(directChatRoomService.getDirectChatRooms(memberId));
    }

    @GetMapping("/chat-rooms/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getDirectChatRoomMessages(
            @RequestBody @Valid ChatMessageRequestDto dto
    ) {
        Long memberId = 1L;

        return ResponseEntity.status(HttpStatus.OK)
                .body(directChatRoomService.getDirectChatRoomMessages(memberId, dto));
    }

    @DeleteMapping("/chat-rooms/{chat-room-id}/direct/leave")
    public ResponseEntity<Void> leaveDirectChatRoom(
            @PathVariable("chat-room-id") @Positive Long chatRoomId
    ) {
        Long memberId = 1L;
        directChatRoomService.leaveDirectChatRoom(chatRoomId, memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
