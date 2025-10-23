package com.mobble.mobbleserver.infrastructure.websocket;

import com.mobble.mobbleserver.application.chat.message.port.required.PublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublisherAdapter implements PublisherPort {

    private final SimpMessagingTemplate template;

    @Override
    public void publishToRoom(Long chatRoomId, Object payload) {
        template.convertAndSend("/topic/rooms/" + chatRoomId, payload);
    }
}
