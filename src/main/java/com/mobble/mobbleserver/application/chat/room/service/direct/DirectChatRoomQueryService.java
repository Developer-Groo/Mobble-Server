package com.mobble.mobbleserver.application.chat.room.service.direct;

import com.mobble.mobbleserver.application.chat.room.port.provided.direct.DirectChatRoomQueryPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.ChatRoomParticipant;
import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.chat.chatMessage.repository.ChatMessageRepository;
import com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.repository.ChatRoomParticipantRepository;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.response.DirectChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.validator.DirectChatRoomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DirectChatRoomQueryService implements DirectChatRoomQueryPort {

    private final MemberReadPort memberReadPort;

    private final DirectChatRoomValidator directChatRoomValidator;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;

    @Override
    public List<DirectChatRoomPreviewResponseDto> getDirectChatRoomsPreview(Long memberId) {
        Member member = findMemberByMemberIdOrThrow(memberId);

        List<DirectRoomInfo> directRoomInfos = directChatRoomValidator.findDirectChatRoomsAllByMemberId(memberId);
        List<Long> chatRoomIds = extractChatRoomIds(directRoomInfos);

        Map<Long, Long> lastReadMessageIdsByChatRoom = getLastReadMessageIdsByChatRoom(chatRoomIds, member.getId());
        Map<Long, ChatMessage> latestMessageMap = chatMessageRepository.findLatestMessagesByChatRoomIds(chatRoomIds);
        Map<Long, Integer> unreadCountMap = chatMessageRepository.countUnreadMessagesByChatRoomIds(chatRoomIds, lastReadMessageIdsByChatRoom);

        return directRoomInfos.stream()
                .map(directRoomInfo -> {
                    ChatRoom chatRoom = directRoomInfo.getChatRoom();
                    Long chatRoomId = chatRoom.getId();

                    Member receiver = chatRoom.getReceiverFor(member.getId());

                    ChatMessage lastMessage = latestMessageMap.get(chatRoomId);
                    int unreadCount = unreadCountMap.getOrDefault(chatRoomId, 0);
                    Long lastReadMessageId = lastReadMessageIdsByChatRoom.getOrDefault(chatRoomId, 0L);

                    return DirectChatRoomPreviewResponseDto.toDto(chatRoom, receiver, lastMessage, unreadCount, lastReadMessageId);
                })
                .toList();
    }

    private List<Long> extractChatRoomIds(List<DirectRoomInfo> directRoomInfos) {
        return directRoomInfos.stream()
                .map(chatRoom -> chatRoom.getChatRoom().getId())
                .toList();
    }

    private Map<Long, Long> getLastReadMessageIdsByChatRoom(List<Long> chatRoomIds, Long memberId) {
        return chatRoomParticipantRepository.findAllByChatRoomIdsAndMemberId(chatRoomIds, memberId)
                .stream()
                .collect(Collectors.toMap(
                        participant -> participant.getChatRoom().getId(),
                        ChatRoomParticipant::getLastReadMessageId
                ));
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
