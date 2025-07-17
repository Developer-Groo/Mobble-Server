package com.mobble.mobbleserver.domain.chat.clubChatRoom.controller;

import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.request.ClubChatRoomRequestDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.service.ClubChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubChatRoomController {

    private final ClubChatRoomService clubChatRoomService;

    @MessageMapping("/group/chat/send")
    public void handleMessage(ClubChatRoomRequestDto dto) {
        Long memberId = 1L;

        clubChatRoomService.sendGroupMessage(dto, memberId);
    }
}
