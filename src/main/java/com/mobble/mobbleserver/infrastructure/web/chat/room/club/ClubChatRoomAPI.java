package com.mobble.mobbleserver.infrastructure.web.chat.room.club;

import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomQueryPort;
import com.mobble.mobbleserver.infrastructure.web.chat.room.club.dto.response.ClubChatRoomPreviewResponseDto;
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
@RequestMapping("/api")
public class ClubChatRoomAPI {

    private final ClubChatRoomQueryPort clubChatRoomQueryPort;

    @GetMapping("/chat-rooms/clubs")
    public ResponseEntity<List<ClubChatRoomPreviewResponseDto>> getClubChatRooms(
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clubChatRoomQueryPort.getClubChatRoomsPreview(memberId));
    }
}
