package com.mobble.mobbleserver.refactor.chat.directChatRoom.controller;

import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.request.ChatMessageRequestDto;
import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.infrastructure.web.chat.room.direct.dto.request.DirectChatMessageRequestDto;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.service.DirectChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestBody DirectChatMessageRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        directChatRoomService.sendDirectMessage(dto, memberId);
    }

    @GetMapping("/chat-rooms/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getDirectChatRoomMessages(
            @RequestBody @Valid ChatMessageRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(directChatRoomService.getDirectChatRoomMessages(memberId, dto));
    }
}
