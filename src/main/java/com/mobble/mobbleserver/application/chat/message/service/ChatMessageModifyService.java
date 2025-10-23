package com.mobble.mobbleserver.application.chat.message.service;

import com.mobble.mobbleserver.application.chat.message.port.provided.SendMessagePort;
import com.mobble.mobbleserver.application.chat.message.port.required.MessageWritePort;
import com.mobble.mobbleserver.application.chat.message.port.required.PublisherPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.web.chat.message.dto.request.ChatMessageRequestDto;
import com.mobble.mobbleserver.infrastructure.web.chat.message.dto.response.ChatMessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageModifyService implements SendMessagePort {

    private final MessageWritePort messageWritePort;

    private final ChatRoomReadPort chatRoomReadPort;
    private final MemberReadPort memberReadPort;

    private final PublisherPort publisherPort;

    @Override
    public void send(Long chatRoomId, Long senderId, ChatMessageRequestDto dto) {
        Member sender = memberReadPort.findByIdAndIsDeletedFalse(senderId).orElseThrow();
        ChatRoom chatRoom = chatRoomReadPort.findChatRoomById(chatRoomId).orElseThrow();

        if (chatRoom.hasParticipant(sender)) throw new IllegalStateException();

        ChatMessage chatMessage = ChatMessage.create(chatRoom, sender, dto.content(), dto.type());
        ChatMessage savedMessage = messageWritePort.save(chatMessage);

        chatRoom.updateLastReadMessage(sender, savedMessage.getId());

        ChatMessageResponseDto response = ChatMessageResponseDto.toDto(savedMessage);

        publisherPort.publishToRoom(chatRoom.getId(), response);
    }
}
