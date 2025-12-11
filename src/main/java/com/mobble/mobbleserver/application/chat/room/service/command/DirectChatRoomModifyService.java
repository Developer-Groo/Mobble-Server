package com.mobble.mobbleserver.application.chat.room.service.command;

import com.mobble.mobbleserver.application.chat.room.error.ChatRoomBusinessError;
import com.mobble.mobbleserver.application.chat.room.port.provided.command.direct.DirectChatRoomCreatePort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomWritePort;
import com.mobble.mobbleserver.application.chat.room.result.DirectChatRoomPreviewResult;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.member.error.MemberBusinessError;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DirectChatRoomModifyService implements DirectChatRoomCreatePort {

    private final ChatRoomWritePort chatRoomWritePort;

    private final MemberReadPort memberReadPort;
    private final ChatRoomReadPort chatRoomReadPort;

    @Override
    public DirectChatRoomPreviewResult create(Long receiverId, Long memberId) {
        // Todo: DB Unique 제약 필요 (memberA + memberB)
        assertDirectChatRoomNotExistsBetweenMembers(receiverId, memberId);

        Member sender = assertMemberByMemberId(memberId);
        Member receiver = assertMemberByMemberId(receiverId);

        ChatRoom directChatRoom = ChatRoom.createDirect(sender, receiver);
        directChatRoom.addParticipant(sender);
        directChatRoom.addParticipant(receiver);

        chatRoomWritePort.save(directChatRoom);

        return DirectChatRoomPreviewResult.create(directChatRoom, receiver);
    }

    /* ==== Private Helper ==== */
    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new BusinessException(MemberBusinessError.NOT_FOUND));
    }

    private void assertDirectChatRoomNotExistsBetweenMembers(Long senderId, Long receiverId) {
        if (chatRoomReadPort.existsDirectChatRoomByBetweenMembers(senderId, receiverId)) throw new BusinessException(ChatRoomBusinessError.ALREADY_EXISTS);
    }
}
