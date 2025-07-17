package com.mobble.mobbleserver.domain.chat.directChatRoom.controller;

import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.request.DirectChatRoomRequestDto;
import com.mobble.mobbleserver.domain.chat.directChatRoom.service.DirectChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DirectChatRoomController {

    private final DirectChatRoomService directChatRoomService;

    @MessageMapping("/direct/chat/send")
    public void handleMessage(DirectChatRoomRequestDto dto) {
        Long memberId = 1L;

        directChatRoomService.sendDirectMessage(dto, memberId);
    }
}
