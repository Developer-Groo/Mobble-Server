package com.mobble.mobbleserver.infrastructure.web.chat.room.direct;

import com.mobble.mobbleserver.application.chat.room.port.provided.command.participant.ChatRoomExitPort;
import com.mobble.mobbleserver.application.chat.room.port.provided.command.direct.DirectChatRoomCreatePort;
import com.mobble.mobbleserver.infrastructure.web.chat.room.direct.dto.request.DirectChatRoomCreateRequestDto;
import com.mobble.mobbleserver.infrastructure.web.chat.room.direct.dto.response.DirectChatRoomPreviewResponseDto;
import jakarta.validation.Valid;
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
@RequestMapping("/api")
public class DirectChatRoomAPI {

    private final DirectChatRoomCreatePort directChatRoomCreatePort;
    private final ChatRoomExitPort chatRoomExitPort;

    @PostMapping("/chat/rooms/direct")
    public ResponseEntity<DirectChatRoomPreviewResponseDto> create(
            @RequestBody @Valid DirectChatRoomCreateRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(directChatRoomCreatePort.create(dto, memberId));
    }

    @DeleteMapping("/chat/rooms/{chat-room-id}/direct/leave")
    public ResponseEntity<Void> leave(
            @PathVariable("chat-room-id") @Positive Long chatRoomId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        chatRoomExitPort.leave(chatRoomId, memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
