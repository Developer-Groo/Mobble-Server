package com.mobble.mobbleserver.application.chat.message.service;

import com.mobble.mobbleserver.application.chat.message.command.SendMessageCommand;
import com.mobble.mobbleserver.application.chat.message.port.provided.SendMessagePort;
import com.mobble.mobbleserver.application.chat.message.port.required.MessageWritePort;
import com.mobble.mobbleserver.application.chat.message.port.required.PublisherPort;
import com.mobble.mobbleserver.application.chat.message.result.ChatMessageResult;
import com.mobble.mobbleserver.application.chat.room.error.ChatRoomBusinessError;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.member.error.MemberBusinessError;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.member.Member;
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
    public void send(SendMessageCommand command) {
        Member sender = assertMemberByMemberId(command.senderId());
        ChatRoom chatRoom = assertChatRoomByChatRoomId(command.chatRoomId());

        assertHasParticipant(chatRoom, sender);

        ChatMessage chatMessage = ChatMessage.create(
                chatRoom,
                sender,
                command.content(),
                command.type(),
                command.mentionedMemberIds()
        );

        ChatMessage savedMessage = messageWritePort.save(chatMessage);

        chatRoom.updateLastReadMessage(sender, savedMessage.getId());

        ChatMessageResult result = ChatMessageResult.create(savedMessage);

        publisherPort.publishToRoom(chatRoom.getId(), result);
    }

    /* ==== Private Helper ==== */
    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new BusinessException(MemberBusinessError.NOT_FOUND));
    }

    private ChatRoom assertChatRoomByChatRoomId(Long chatRoomId) {
        return chatRoomReadPort.findChatRoomById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ChatRoomBusinessError.NOT_FOUND));
    }

    private void assertHasParticipant(ChatRoom chatRoom, Member member) {
        if (!chatRoom.hasParticipant(member)) throw new BusinessException(ChatRoomBusinessError.NOT_PARTICIPANT);
    }
}
