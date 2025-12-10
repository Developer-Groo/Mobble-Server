package com.mobble.mobbleserver.application.chat.room.service.command;

import com.mobble.mobbleserver.application.chat.message.port.required.MessageWritePort;
import com.mobble.mobbleserver.application.chat.room.port.provided.command.participant.ChatRoomExitPort;
import com.mobble.mobbleserver.application.chat.room.port.provided.command.participant.ParticipantUpdatePort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomWritePort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.member.error.MemberBusinessError;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.ChatRoomType;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParticipantModifyService implements ParticipantUpdatePort, ChatRoomExitPort {

    private final ChatRoomWritePort chatRoomWritePort;
    private final MessageWritePort messageWritePort;

    private final ChatRoomReadPort chatRoomReadPort;
    private final MemberReadPort memberReadPort;

    @Override
    public void updateLastReadMessage(Long chatRoomId, Long memberId, Long lastMessageId) {
        Member member = assertMemberByMemberId(memberId);
        ChatRoom chatRoom = assertChatRoomByChatRoomId(chatRoomId);

        chatRoom.updateLastReadMessage(member, lastMessageId);
    }

    @Override
    public void updateNotification(Long chatRoomId, Long memberId, boolean enabled) {
        Member member = assertMemberByMemberId(memberId);
        ChatRoom chatRoom = assertChatRoomByChatRoomId(chatRoomId);

        if (enabled) {
            chatRoom.enableNotified(member);
        } else {
            chatRoom.disableNotified(member);
        }
    }

    @Override
    public void leave(Long chatRoomId, Long memberId) {
        Member member = assertMemberByMemberId(memberId);
        ChatRoom chatRoom = assertChatRoomByChatRoomId(chatRoomId);

        chatRoom.removeParticipant(member);

        if (chatRoom.getType() == ChatRoomType.DIRECT && chatRoom.getParticipants().isEmpty()) {
            delete(chatRoom.getId());
        }
    }

    @Override
    public void delete(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomReadPort.findChatRoomById(chatRoomId)
                .orElse(null);

        if (chatRoom == null) return;

        messageWritePort.delete(chatRoom.getId());
        chatRoomWritePort.delete(chatRoom);
    }

    /* ==== Private Helper ==== */
    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new BusinessException(MemberBusinessError.NOT_FOUND));
    }

    private ChatRoom assertChatRoomByChatRoomId(Long chatRoomId) {
        return chatRoomReadPort.findChatRoomById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Error 수정
    }
}
