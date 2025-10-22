package com.mobble.mobbleserver.infrastructure.web.chat.message;

import com.mobble.mobbleserver.infrastructure.web.chat.message.dto.request.ChatMessageRequestDto;
import com.mobble.mobbleserver.infrastructure.web.chat.message.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.service.ClubChatRoomService;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.service.DirectChatRoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatMessageAPI {
    // Todo: api 병합 설계 필요

    private final ClubChatRoomService clubChatRoomService;
    private final DirectChatRoomService directChatRoomService;

    @GetMapping("/clubs/{club-id}/chat-rooms/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getClubChatRoomMessages(
            @PathVariable(name = "club-id") @Positive Long clubId,
            @RequestBody @Valid ChatMessageRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clubChatRoomService.getClubChatRoomMessages(clubId, memberId, dto));
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
