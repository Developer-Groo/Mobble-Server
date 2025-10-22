package com.mobble.mobbleserver.application.chat.room.service.common;

import com.mobble.mobbleserver.application.chat.room.port.provided.common.ChatRoomExitPort;
import com.mobble.mobbleserver.application.chat.room.port.provided.common.ParticipantUpdatePort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.ChatRoomType;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.persistence.chat.message.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomModifyService implements ParticipantUpdatePort, ChatRoomExitPort {

    private final ChatRoomWritePort chatRoomWritePort;

    private final ChatRoomReadPort chatRoomReadPort;
    private final MemberReadPort memberReadPort;

    // Todo: port 변경 필요
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public void updateLastReadMessage(Long chatRoomId, Long memberId, Long lastMessageId) {
        Member member = memberReadPort.findByIdAndIsDeletedFalse(memberId).orElseThrow();
        ChatRoom chatRoom = chatRoomReadPort.findChatRoomById(chatRoomId).orElseThrow();

        chatRoom.updateLastReadMessage(member, lastMessageId);
    }

    @Override
    public void updateNotification(Long chatRoomId, Long memberId, boolean enabled) {
        Member member = memberReadPort.findByIdAndIsDeletedFalse(memberId).orElseThrow();
        ChatRoom chatRoom = chatRoomReadPort.findChatRoomById(chatRoomId).orElseThrow();

        if (enabled) {
            chatRoom.enableNotified(member);
        } else {
            chatRoom.disableNotified(member);
        }
    }

    @Override
    public void leave(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomReadPort.findChatRoomById(chatRoomId).orElseThrow();
        Member member = memberReadPort.findByIdAndIsDeletedFalse(memberId).orElseThrow();

        chatRoom.removeParticipant(member);

        if (chatRoom.getType() == ChatRoomType.DIRECT && chatRoom.getParticipants().isEmpty()) {
            delete(chatRoom.getId());
        }
    }

    @Override
    public void delete(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomReadPort.findChatRoomById(chatRoomId).orElse(null);
        if (chatRoom == null) return;

        chatMessageRepository.deleteByChatRoomId(chatRoom.getId());
        chatRoomWritePort.delete(chatRoom);
    }
}
