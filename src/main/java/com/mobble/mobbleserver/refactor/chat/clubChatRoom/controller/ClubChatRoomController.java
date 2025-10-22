package com.mobble.mobbleserver.refactor.chat.clubChatRoom.controller;

import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomQueryPort;
import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.request.ChatMessageRequestDto;
import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.infrastructure.web.chat.room.club.dto.request.ClubChatMessageRequestDto;
import com.mobble.mobbleserver.infrastructure.web.chat.room.club.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.service.ClubChatRoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
public class ClubChatRoomController {

    private final ClubChatRoomService clubChatRoomService;

    @MessageMapping("/group/chat/send")
    public void handleMessage(
            ClubChatMessageRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        clubChatRoomService.sendGroupMessage(dto, memberId);
    }

    @GetMapping("/clubs/{club-id}/chat-rooms/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getClubChatRoomMessages(
            @PathVariable(name = "club-id") @Positive Long clubId,
            @RequestBody @Valid ChatMessageRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clubChatRoomService.getClubChatRoomMessages(clubId, memberId, dto));
    }
}
