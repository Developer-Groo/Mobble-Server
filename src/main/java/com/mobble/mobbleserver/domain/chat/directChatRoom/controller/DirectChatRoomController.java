package com.mobble.mobbleserver.domain.chat.directChatRoom.controller;

import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.request.DirectChatMessageRequestDto;
import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.request.DirectChatRoomCreateRequestDto;
import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.response.DirectChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.domain.chat.directChatRoom.service.DirectChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
