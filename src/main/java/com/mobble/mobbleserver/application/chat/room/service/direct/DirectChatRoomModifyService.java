package com.mobble.mobbleserver.application.chat.room.service.direct;

import com.mobble.mobbleserver.application.chat.room.port.provided.direct.DirectChatRoomCreatePort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.request.DirectChatRoomCreateRequestDto;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.response.DirectChatRoomPreviewResponseDto;
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
    public DirectChatRoomPreviewResponseDto createDirectChatRoom(DirectChatRoomCreateRequestDto dto, Long memberId) {
        // Todo: DB Unique 제약 필요 (memberA + memberB)
        if (chatRoomReadPort.existsDirectChatRoomByBetweenMembers(memberId, dto.receiverId())) throw new IllegalStateException();

        Member sender = findMemberByMemberIdOrThrow(memberId);
        Member receiver = findMemberByMemberIdOrThrow(dto.receiverId());

        ChatRoom directChatRoom = ChatRoom.createDirect(sender, receiver);
        directChatRoom.addParticipant(sender);
        directChatRoom.addParticipant(receiver);

        chatRoomWritePort.save(directChatRoom);

        return DirectChatRoomPreviewResponseDto.toDto(directChatRoom, receiver, null, 0, null);
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
