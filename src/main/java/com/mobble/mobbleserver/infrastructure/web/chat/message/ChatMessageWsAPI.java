package com.mobble.mobbleserver.infrastructure.web.chat.message;

import com.mobble.mobbleserver.infrastructure.web.chat.room.club.dto.request.ClubChatMessageRequestDto;
import com.mobble.mobbleserver.infrastructure.web.chat.room.direct.dto.request.DirectChatMessageRequestDto;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.service.ClubChatRoomService;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.service.DirectChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatMessageWsAPI {
    // Todo: api 병합 설계 필요

    private final ClubChatRoomService clubChatRoomService;
    private final DirectChatRoomService directChatRoomService;

    @MessageMapping("/group/chat/send")
    public void handleMessage(
            ClubChatMessageRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        clubChatRoomService.sendGroupMessage(dto, memberId);
    }

    @MessageMapping("/direct/chat/send")
    public void handleMessage(
            @RequestBody DirectChatMessageRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        directChatRoomService.sendDirectMessage(dto, memberId);
    }
}
