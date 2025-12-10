package com.mobble.mobbleserver.infrastructure.web.chat.room.query;

import com.mobble.mobbleserver.application.chat.room.port.provided.query.ChatRoomQueryPort;
import com.mobble.mobbleserver.application.chat.room.result.ChatRoomPreviewResult;
import com.mobble.mobbleserver.application.chat.room.service.query.ChatRoomPreviewFilter;
import com.mobble.mobbleserver.infrastructure.web.chat.room.query.dto.ChatRoomPreviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/rooms")
public class ChatRoomAPI {

    private final ChatRoomQueryPort chatRoomQueryPort;

    @GetMapping
    public ResponseEntity<ChatRoomPreviewResponseDto> getAllChatRooms(
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        List<ChatRoomPreviewResult> result = chatRoomQueryPort.getChatRoomsPreview(memberId, ChatRoomPreviewFilter.ALL);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ChatRoomPreviewResponseDto.toDto(result));
    }

    @GetMapping("/club")
    public ResponseEntity<ChatRoomPreviewResponseDto> getClubChatRooms(
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        List<ChatRoomPreviewResult> result = chatRoomQueryPort.getChatRoomsPreview(memberId, ChatRoomPreviewFilter.CLUB_ONLY);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ChatRoomPreviewResponseDto.toDto(result));
    }

    @GetMapping("/direct")
    public ResponseEntity<ChatRoomPreviewResponseDto> getDirectChatRooms(
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        List<ChatRoomPreviewResult> result = chatRoomQueryPort.getChatRoomsPreview(memberId, ChatRoomPreviewFilter.DIRECT_ONLY);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ChatRoomPreviewResponseDto.toDto(result));
    }
}
