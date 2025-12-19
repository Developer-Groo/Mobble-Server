package com.mobble.mobbleserver.infrastructure.web.chat.message;

import com.mobble.mobbleserver.application.chat.message.command.SendMessageCommand;
import com.mobble.mobbleserver.application.chat.message.port.provided.SendMessagePort;
import com.mobble.mobbleserver.infrastructure.web.chat.message.dto.request.ChatMessageRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatMessageWsAPI {

    private final SendMessagePort sendMessagePort;

    @MessageMapping("/rooms/{chat-room-id}/messages/send")
    public void handleMessage(
            @DestinationVariable Long chatRoomId,
            @RequestBody @Valid ChatMessageRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        SendMessageCommand command = dto.toCommand(chatRoomId, memberId);

        sendMessagePort.send(command);
    }
}
